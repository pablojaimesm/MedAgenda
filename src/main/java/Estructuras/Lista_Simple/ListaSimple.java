package Estructuras.Lista_Simple;

import java.util.Iterator;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import Modelos.Cita;

public class ListaSimple<T extends Comparable<T>> implements Lista<T>, Iterable<T> {

    private Nodo<T> head;  // Puntero al primer nodo de la lista
    private int size;      // Tamaño de la lista

    // Constructor para inicializar la lista vacía
    public ListaSimple() {
        this.setHead(null);
        this.size = 0;
    }

    public Nodo<T> getHead() {
		return head;
	}

	public void setHead(Nodo<T> head) {
		this.head = head;
	}

	@Override
    public int size() {
        return size;
    }

    @Override
    public void append(T valor) {
        if (getHead() == null) {  // Si la lista está vacía, el nuevo nodo se convierte en la head
            setHead(new Nodo<>(valor));
            size++;
            return;
        }
        Nodo<T> current = getHead();
        while (current.getNext() != null) {
            current = current.getNext();  // Avanzar hasta el último nodo
        }
        current.setNext(new Nodo<>(valor));  // Agregar el nuevo nodo al final
        size++;
    }
    
    public Nodo<T> getTail() {
        if (isEmpty()) {
            return null; // La lista está vacía
        }

        Nodo<T> current = getHead();
        while (current.getNext() != null) {
            current = current.getNext(); // Avanzar al siguiente nodo
        }
        return current; // Devolver el último nodo
    }


    @Override
    public Iterator<T> iterator() {
        return new ListaSimpleIterator();
    }

    // Clase interna para el iterador de ListaSimple
    private class ListaSimpleIterator implements Iterator<T> {
        private Nodo<T> current = getHead();

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            T value = current.getValue();
            current = current.getNext();
            return value;
        }
    }

    // Método para eliminar un elemento de la lista
    @Override
    public void remove(T elemento) {
        if (getHead() == null) {
            return; // La lista está vacía, no hay nada que eliminar
        }

        if (getHead().getValue().equals(elemento)) {
            // El elemento a eliminar es el head en la lista
            setHead(getHead().getNext());
            size--;
            return;
        }

        Nodo<T> actual = getHead();
        while (actual.getNext() != null) {
            if (actual.getNext().getValue().equals(elemento)) {
                // El elemento a eliminar está en el siguiente nodo
                actual.setNext(actual.getNext().getNext());
                size--;
                return;
            }
            actual = actual.getNext();
        }
    }

    // Resto de los métodos de la lista (append, prepend, insert, get_element_at, toString, sort, etc.)

    
    @Override
    public void sort(Comparator<T> comparator) {
        if (getHead() == null || getHead().getNext() == null) {
            return;
        }

        setHead(mergeSort(getHead(), comparator));
    }

    // Método auxiliar para realizar Merge Sort en la lista enlazada con un comparador
    private Nodo<T> mergeSort(Nodo<T> start, Comparator<T> comparator) {
        if (start == null || start.getNext() == null) {
            return start;
        }

        Nodo<T> middle = getMiddle(start);
        Nodo<T> nextToMiddle = middle.getNext();
        middle.setNext(null);

        Nodo<T> left = mergeSort(start, comparator);
        Nodo<T> right = mergeSort(nextToMiddle, comparator);

        return merge(left, right, comparator);
    }

    // Método auxiliar para combinar dos listas ordenadas en una sola lista ordenada con un comparador
    private Nodo<T> merge(Nodo<T> left, Nodo<T> right, Comparator<T> comparator) {
        Nodo<T> result = null;

        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }

        if (comparator.compare(left.getValue(), right.getValue()) <= 0) {
            result = left;
            result.setNext(merge(left.getNext(), right, comparator));
        } else {
            result = right;
            result.setNext(merge(left, right.getNext(), comparator));
        }

        return result;
    }

    // Método para agregar al principio de la lista
    @Override
    public void prepend(T valor) {
        Nodo<T> nuevoNodo = new Nodo<>(valor, getHead());  // Crear un nuevo nodo con el siguiente apuntando a la head
        setHead(nuevoNodo);  // El nuevo nodo se convierte en la head
    }

    // Método para insertar en una posición específica
    @Override
    public void insert(T valor, int indice) {
        if (indice < 0 || indice > size() - 1) { // Modificación en la validación del índice
            throw new IndexOutOfBoundsException("Índice fuera de rango");
        }
        if (indice == 0) {
            prepend(valor);
            return;
        }
        Nodo<T> current = getHead();
        for (int i = 0; i < indice - 1; i++) {
            if (current == null) {
                throw new IndexOutOfBoundsException("Índice fuera de rango");
            }
            current = current.getNext();
        }
        Nodo<T> nuevoNodo = new Nodo<>(valor, current.getNext());
        current.setNext(nuevoNodo);
    }

    // Método para obtener el valor en una posición específica
    @Override
    public T get_element_at(int indice) {
        Nodo<T> current = getHead();
        for (int i = 0; i < indice; i++) {
            if (current == null) {
                throw new IndexOutOfBoundsException("Índice fuera de rango");
            }
            current = current.getNext();  // Avanzar hasta el nodo en la posición indicada
        }
        return current.getValue();  // Devolver el valor del nodo en la posición indicada
    }

    // Método para representar la lista como una cadena
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Nodo<T> current = getHead();
        while (current != null) {
            sb.append(current.getValue());
            if (current.getNext() != null) {
                sb.append(" -> ");
            }
            current = current.getNext();  // Avanzar al siguiente nodo
        }
        return sb.toString();
    }

    // Método para ordenar la lista
    @Override
    public void sort() {
        if (getHead() == null || getHead().getNext() == null) {
            // La lista está vacía o tiene un solo elemento, ya está ordenada
            return;
        }

        setHead(mergeSort(getHead()));
    }

    // Método auxiliar para realizar Merge Sort en la lista enlazada
    private Nodo<T> mergeSort(Nodo<T> start) {
        if (start == null || start.getNext() == null) {
            return start;
        }

        // Dividir la lista en dos partes
        Nodo<T> middle = getMiddle(start);
        Nodo<T> nextToMiddle = middle.getNext();
        middle.setNext(null);

        // Aplicar mergeSort a ambas partes
        Nodo<T> left = mergeSort(start);
        Nodo<T> right = mergeSort(nextToMiddle);

        // Combinar las dos partes ordenadas
        return merge(left, right);
    }

    // Método auxiliar para combinar dos listas ordenadas en una sola lista ordenada
    private Nodo<T> merge(Nodo<T> left, Nodo<T> right) {
        Nodo<T> result = null;

        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }

        if (left.getValue().compareTo(right.getValue()) <= 0) {
            result = left;
            result.setNext(merge(left.getNext(), right));
        } else {
            result = right;
            result.setNext(merge(left, right.getNext()));
        }

        return result;
    }

    // Método auxiliar para encontrar el nodo medio de la lista
    private Nodo<T> getMiddle(Nodo<T> start) {
        if (start == null) {
            return start;
        }

        Nodo<T> slow = start;
        Nodo<T> fast = start;

        while (fast.getNext() != null && fast.getNext().getNext() != null) {
            slow = slow.getNext();
            fast = fast.getNext().getNext();
        }

        return slow;
    }

    // Método para buscar elementos por condición
    @Override
    public Lista<T> filter(Predicate<T> predicate) {
        Lista<T> result = new ListaSimple<>();
        Nodo<T> current = getHead();
        while (current != null) {
            if (predicate.test(current.getValue())) {
                result.append(current.getValue());
            }
            current = current.getNext();
        }
        return result;
    }

 // Método para eliminar elementos duplicados manteniendo el orden
    @Override
    public void removeDuplicates() {
        Set<T> uniqueElements = new HashSet<>();
        Nodo<T> current = getHead();
        Nodo<T> prev = null;

        while (current != null) {
            if (uniqueElements.contains(current.getValue())) {
                if (prev != null) {
                    prev.setNext(current.getNext());
                } else {
                    setHead(current.getNext());
                }
            } else {
                uniqueElements.add(current.getValue());
                prev = current;
            }
            current = current.getNext();
        }
    }

    @Override
    public int compareTo(Lista<T> o) {
        return Integer.compare(this.size(), o.size());
    }

    public boolean isEmpty() {
        return head == null;
    }

    public T getElementoEnPosicion(int indiceSeleccionado) {
        // Verificar si el índice seleccionado es válido
        if (indiceSeleccionado < 0 || indiceSeleccionado >= size) {
            throw new IndexOutOfBoundsException("Índice fuera de los límites de la lista");
        }

        // Iniciar un contador para recorrer la lista
        int contador = 0;
        Nodo<T> nodoActual = head;

        // Recorrer la lista hasta llegar a la posición deseada
        while (contador < indiceSeleccionado) {
            nodoActual = nodoActual.getNext();
            contador++;
        }

        // Devolver el elemento en la posición deseada
        return nodoActual.getValue();
    }




    
    


}
