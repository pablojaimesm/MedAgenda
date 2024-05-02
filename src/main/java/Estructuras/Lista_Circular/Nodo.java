package Estructuras.Lista_Circular;

public class Nodo<T> {
    private T value;
    private Nodo<T> next;
    private Nodo<T> previous;

    public Nodo(T value) {
        this.value = value;
        this.next = null;
        this.previous = null;
    }

    public T getValue() {
        return value;
    }

    public Nodo<T> getNext() {
        return next;
    }

    public void setNext(Nodo<T> next) {
        this.next = next;
    }

    public Nodo<T> getPrevious() {
        return previous;
    }

    public void setPrevious(Nodo<T> previous) {
        this.previous = previous;
    }

    public boolean isEqual(T value) {
        return this.value.equals(value);
    }

	public void setValue(T value) {
		this.value = value;
	}
}
