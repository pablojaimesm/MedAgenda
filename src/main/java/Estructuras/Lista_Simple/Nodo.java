package Estructuras.Lista_Simple;

// Inner Node Class
public class Nodo<T> {

    private T value;            // Value stored in the node
    private Nodo<T> next;       // Pointer to the next node in the list

    // Constructor for a node without a next node
    public Nodo(T value) {
        this(value, null);
    }

    // Constructor for a node with a next node
    public Nodo(T value, Nodo<T> next) {
        this.value = value;
        this.next = next;
    }

    // Access methods
    public T getValue() {
        return value;
    }
    
    

    public void setValue(T value) {
		this.value = value;
	}

	public Nodo<T> getNext() {
        return next;
    }

    public void setNext(Nodo<T> next) {
        this.next = next;
    }
    
    /* In summary, the Node class encapsulates the logic to represent individual nodes of the linked list 
    and facilitates manipulation and navigation within the list structure. */
}
