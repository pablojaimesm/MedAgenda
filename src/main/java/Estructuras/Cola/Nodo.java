package Estructuras.Cola;

public class Nodo<T> {

    private T elemento;
    private Nodo<T> siguiente;

    // Constructor de la clase Nodo
    public Nodo(T elemento) {
        this.elemento = elemento;
        this.siguiente = null;
    }

    // Método para obtener el elemento almacenado en el nodo
    public T getElemento() {
        return elemento;
    }

    // Método para obtener el nodo siguiente
    public Nodo<T> getSiguiente() {
        return siguiente;
    }

    // Método para establecer el nodo siguiente
    public void setSiguiente(Nodo<T> siguiente) {
        this.siguiente = siguiente;
    }

    // Método toString para representar el contenido del nodo como una cadena
    @Override
    public String toString() {
        return "Nodo [elemento=" + elemento + ", siguiente=" + siguiente + "]";
    }
}
