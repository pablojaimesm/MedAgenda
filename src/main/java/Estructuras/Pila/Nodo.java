package Estructuras.Pila;

// Clase Nodo parametrizada por tipo (generics)
public class Nodo<T> {
    // Atributos de la clase Nodo
    private T elemento; // Elemento almacenado en el nodo
    private Nodo<T> siguiente; // Referencia al siguiente nodo en la secuencia

    // Constructor de la clase Nodo
    public Nodo(T elemento) {
        this.elemento = elemento; // Asignar el elemento proporcionado al nodo
        this.siguiente = null; // Inicializar la referencia al siguiente nodo como nula
    }

    // Método para obtener el elemento almacenado en el nodo
    public T getElemento() {
        return elemento; // Devolver el elemento almacenado en el nodo
    }

    // Método para obtener la referencia al siguiente nodo
    public Nodo<T> getSiguiente() {
        return siguiente; // Devolver la referencia al siguiente nodo
    }

    // Método para establecer la referencia al siguiente nodo
    public void setSiguiente(Nodo<T> siguiente) {
        this.siguiente = siguiente; // Establecer la referencia al siguiente nodo
    }
}
