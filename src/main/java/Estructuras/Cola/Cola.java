package Estructuras.Cola;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Cola<T> implements Iterable<T>{

    private Nodo<T> frente;
    private Nodo<T> fin;

    // Constructor de la cola
    public Cola() {
        this.frente = null;
        this.fin = null;
    }

 

    // En la clase Cola<T>
    public void insertBefore(Nodo<T> nodoAnterior, T elemento) {
        Nodo<T> nuevoNodo = new Nodo<>(elemento);
        nuevoNodo.setSiguiente(nodoAnterior.getSiguiente());
        nodoAnterior.setSiguiente(nuevoNodo);
        if (fin == nodoAnterior) {
            fin = nuevoNodo;
        }
    }

    public void enqueue(T elemento) {
        Nodo<T> nuevoNodo = new Nodo<>(elemento);
        if (estaVacia()) {
            frente = nuevoNodo;
        } else {
            fin.setSiguiente(nuevoNodo);
        }
        fin = nuevoNodo;
    }


    // Método para quitar y obtener el elemento al frente de la cola (desencolar)
    public T dequeue() {
        if (estaVacia()) {
            return null;
        }

        T elementoDesencolado = frente.getElemento();
        frente = frente.getSiguiente();

        if (frente == null) {
            fin = null;
        }

        return elementoDesencolado;
    }

    // Método para obtener el elemento al frente de la cola sin quitarlo
    public T peek() {
        return estaVacia() ? null : frente.getElemento();
    }

    // Método para verificar si la cola está vacía
    public boolean estaVacia() {
        return frente == null;
    }

    // Método para mostrar el contenido de la cola
    public void displayCola() {
        Nodo<T> current = frente;
        while (current != null) {
            System.out.print(current.getElemento() + " ");
            current = current.getSiguiente();
        }
        System.out.println();
    }

    // Método para verificar si la cola contiene un elemento específico
    public boolean contains(T elemento) {
        Nodo<T> current = frente;
        while (current != null) {
            if (current.getElemento().equals(elemento)) {
                return true;
            }
            current = current.getSiguiente();
        }
        return false;
    }

    // Método para eliminar un elemento específico de la cola
    public boolean remove(T elemento) {
        if (estaVacia()) {
            return false;
        }

        if (frente.getElemento().equals(elemento)) {
            frente = frente.getSiguiente();
            if (frente == null) {
                fin = null;
            }
            return true;
        }

        Nodo<T> current = frente;
        while (current.getSiguiente() != null && !current.getSiguiente().getElemento().equals(elemento)) {
            current = current.getSiguiente();
        }

        if (current.getSiguiente() != null) {
            current.setSiguiente(current.getSiguiente().getSiguiente());
            if (current.getSiguiente() == null) {
                fin = current;
            }
            return true;
        }

        return false;
    }
    
    public String mostrarCola() {
        StringBuilder builder = new StringBuilder();
        Nodo<T> actual = frente;
        while (actual != null) {
            builder.append(actual.getElemento().toString()).append("\n");
            actual = actual.getSiguiente();
        }
        return builder.toString();
    }

	public Nodo<T> getFrente() {
		return frente;
	}

	public void setFrente(Nodo<T> frente) {
		this.frente = frente;
	}

	public Nodo<T> getFin() {
		return fin;
	}

	public void setFin(Nodo<T> fin) {
		this.fin = fin;
	}
	
	
	@Override
	public Iterator<T> iterator() {
	    return new Iterator<T>() {
	        private Nodo<T> current = frente;

	        @Override
	        public boolean hasNext() {
	            return current != null;
	        }

	        @Override
	        public T next() {
	            if (hasNext()) {
	                T elemento = current.getElemento();
	                current = current.getSiguiente();
	                return elemento;
	            } else {
	                throw new NoSuchElementException("No hay más elementos en la cola");
	            }
	        }
	    };
	}

    
    
}
