package Estructuras.Lista_Circular;

import java.util.Comparator;

public class ListaCircularEnlazada<T> implements Lista<T> {
    private Nodo<T> head;
    private Nodo<T> tail;
    private int size;


    public ListaCircularEnlazada() {
        head = null;
        tail = null;
        size = 0;
    }
    
    
    public Nodo<T> getHead() {
        return head;
    }

    public void set(int index, T value) {
        if (index < 0 || index >= getSize()) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }

        Nodo<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        current.setValue(value);
    }

    @Override
    public T get(int index) throws IndexOutOfBoundsException {
        if (isEmpty() || index < 0 || index >= getSize()) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }

        Nodo<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current.getValue();
    }

    @Override
    public void addToFront(T value) {
        Nodo<T> newNode = new Nodo<>(value);

        if (isEmpty()) {
            this.head = newNode;
            this.tail = newNode;
            newNode.setNext(newNode);
            newNode.setPrevious(newNode);
        } else {
            newNode.setNext(this.head);
            newNode.setPrevious(this.tail);
            this.head.setPrevious(newNode);
            this.tail.setNext(newNode);
            this.head = newNode;
        }
        
        this.size++;
    }




    @Override
    public void addToEnd(T value) {
        Nodo<T> newNode = new Nodo<>(value);
        

        if (isEmpty()) {
            this.head = newNode;
            this.tail = newNode;
            newNode.setNext(newNode);
            newNode.setPrevious(newNode);
            return;
        }

        newNode.setNext(this.head);
        newNode.setPrevious(this.tail);
        this.tail.setNext(newNode);
        this.tail = newNode;
        this.head.setPrevious(newNode);  // Actualiza el previous del head al nuevo tail
        
        this.size++;
    }



    @Override
    public void insertInMiddle(T value) {
        Nodo<T> newNode = new Nodo<>(value);
        

        if (isEmpty()) {
            this.head = newNode;
            this.tail = newNode;
            newNode.setNext(newNode);
            newNode.setPrevious(newNode);
            return;
        }

        // Insert in the middle
        Nodo<T> current = this.head;
        int middle = getSize() / 2;

        for (int i = 0; i < middle; i++) {
            current = current.getNext();
        }

        newNode.setNext(current.getNext());
        newNode.setPrevious(current);
        current.getNext().setPrevious(newNode);
        current.setNext(newNode);
        
        this.size++;
    }


    @Override
    public void removeFromFront() {
        if (!isEmpty()) {
            if (getSize() == 1) {
                head = null;
            } else {
                head.getPrevious().setNext(head.getNext());
                head.getNext().setPrevious(head.getPrevious());
                head = head.getNext();
            }
            size--;
        }
    }

    @Override
    public void removeFromEnd() {
        if (!isEmpty()) {
            if (getSize() == 1) {
                head = null;
            } else {
                head.getPrevious().getPrevious().setNext(head);
                head.setPrevious(head.getPrevious().getPrevious());
            }
            size--;
        }
    }

    @Override
    public void removeFromMiddle() {
        if (!isEmpty()) {
            int middle = getSize() / 2;
            Nodo<T> current = head;

            for (int i = 0; i < middle; i++) {
                current = current.getNext();
            }

            current.getPrevious().setNext(current.getNext());
            current.getNext().setPrevious(current.getPrevious());

            size--;
        }
    }

    @Override
    public int searchElement(T value) {
        if (isEmpty()) {
            return -1;
        }

        Nodo<T> current = head;
        int index = 0;

        do {
            if (current.isEqual(value)) {
                return index;
            }
            current = current.getNext();
            index++;
        } while (current != head);

        return -1;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    public void rotateList(int steps) {
        if (isEmpty() || steps == 0) {
            return; // Nothing to do
        }

        if (steps > 0) {
            // Rotate forward
            for (int i = 0; i < steps; i++) {
                this.head = this.head.getNext();
                this.tail = this.tail.getNext();
            }
        } else {
            // Rotate backward
            for (int i = 0; i < Math.abs(steps); i++) {
                this.head = this.head.getPrevious();
                this.tail = this.tail.getPrevious();
            }
        }
    }

    public void merge(ListaCircularEnlazada<T> otherList) {
        if (otherList == null || otherList.isEmpty()) {
            return; // Nothing to merge
        }

        if (isEmpty()) {
            // If the original list is empty, simply copy the other list
            this.head = otherList.head;
            this.tail = otherList.tail;
            this.size = otherList.size;
        } else {
            // Merge the lists
            Nodo<T> temp = this.tail.getNext();
            this.tail.setNext(otherList.head);
            otherList.head.setPrevious(this.tail);
            otherList.tail.setNext(temp);
            temp.setPrevious(otherList.tail);
            this.tail = otherList.tail;
            this.size += otherList.size;
        }
    }


    public ListaCircularEnlazada<T> splitList(int index) {
        if (isEmpty() || index <= 0 || index >= getSize()) {
            return null; // Invalid index or empty list
        }

        ListaCircularEnlazada<T> splitList = new ListaCircularEnlazada<>();
        Nodo<T> current = this.head;
        int count = 0;

        // Move to the node at the specified index
        while (count < index) {
            current = current.getNext();
            count++;
        }

        // Configure the new list
        splitList.head = current;
        splitList.tail = this.tail;
        splitList.size = this.size - index;

        // Adjust the original list
        this.tail = current.getPrevious();
        this.tail.setNext(this.head);
        this.head.setPrevious(this.tail);
        this.size = index;

        return splitList;
    }


    @Override
    public String displayList() {
        StringBuilder sb = new StringBuilder();

        if (!isEmpty()) {
            Nodo<T> current = head;
            int count = 0;

            do {
                sb.append(current.getValue());
                if (current.getNext() != head) {
                    sb.append(" <-> ");
                }
                current = current.getNext();
                count++;

                // Limita la impresión a 20 elementos para evitar OutOfMemoryError
                if (count >= getSize()) {
                    break;
                }
                
            } while (current != head);

            // Añade un indicador si se han omitido elementos
            if (count < getSize()) {
                sb.append(" ... (se omiten ").append(getSize() - count).append(" elementos)");
            }
        }

        return sb.toString();
    }
    
    
    
    public void insertOrdered(T value, Comparator<T> comparator) {
        if (isEmpty()) {
            // Si la lista está vacía, simplemente agregamos el nuevo valor
            addToFront(value);
            return;
        }

        Nodo<T> newNode = new Nodo<>(value);
        Nodo<T> current = head;
        do {
            // Aquí utilizamos el comparador externo para determinar si el nuevo valor
            // debe insertarse antes o después del valor actual en la lista
            if (comparator.compare(value, current.getValue()) <= 0) {
                // Insertar el nuevo valor antes del valor actual
                newNode.setNext(current);
                newNode.setPrevious(current.getPrevious());
                current.getPrevious().setNext(newNode);
                current.setPrevious(newNode);
                if (current == head) {
                    head = newNode;
                }
                size++;
                return;
            }
            current = current.getNext();
        } while (current != head);

        // Si el nuevo valor debe ir al final de la lista, lo agregamos al final
        addToEnd(value);
    }

    
    
    
 
}
