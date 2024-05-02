package Estructuras.Lista_Doblemente_Enlazada;

public class ListaDobleEnlazada<T> implements Lista<T> {

    private Nodo<T> head;
    private Nodo<T> tail;
    private int size;

    public ListaDobleEnlazada() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public void addToFront(T value) {
        Nodo<T> newNode = new Nodo<>(value);
        this.size++;

        if (isEmpty()) {
            this.head = newNode;
            this.tail = newNode;
            return;
        }

        newNode.setNext(this.head);
        this.head.setPrevious(newNode);
        this.head = newNode;
    }

    @Override
    public void addToEnd(T value) {
        Nodo<T> newNode = new Nodo<>(value);
        this.size++;

        if (isEmpty()) {
            this.head = newNode;
            this.tail = newNode;
            return;
        }

        this.tail.setNext(newNode);
        newNode.setPrevious(this.tail);
        this.tail = newNode;
    }

    @Override
    public void insertInMiddle(T value) {
        Nodo<T> newNode = new Nodo<>(value);
        this.size++;

        if (isEmpty()) {
            this.head = newNode;
            this.tail = newNode;
            return;
        }
        if (this.size == 1) {
            this.tail.setNext(newNode);
            newNode.setPrevious(this.tail);
            return;
        }

        int middle = (this.size / 2) - 1;

        Nodo<T> current = this.head;

        for (int i = 0; i < middle; i++) {
            current = current.getNext();
        }

        Nodo<T> temp = current.getNext();

        current.setNext(newNode);
        if (temp != null) {
            temp.setPrevious(newNode);
        }
        newNode.setNext(temp);
        newNode.setPrevious(current);
    }

    @Override
    public void removeFromFront() {
        if (isEmpty()) return;

        if (this.size == 1) {
            this.head = null;
            this.tail = null;
            this.size--;
            return;
        }

        this.size--;

        this.head = this.head.getNext();
        this.head.setPrevious(null);
    }

    @Override
    public void removeFromEnd() {
        if (isEmpty()) return;

        if (this.size == 1) {
            this.head = null;
            this.tail = null;
            this.size--;
            return;
        }

        this.size--;

        this.tail = this.tail.getPrevious();
        this.tail.setNext(null);
    }

    @Override
    public void removeFromMiddle(T value) {
        if (isEmpty()) return;

        Nodo<T> current = this.head;
        while (current != null) {
            if (current.getValue().equals(value)) {
                Nodo<T> prev = current.getPrevious();
                Nodo<T> next = current.getNext();
                
                if (prev != null) {
                    prev.setNext(next);
                } else {
                    // Si el nodo a eliminar es el primero, actualizamos la cabeza
                    head = next;
                }
                
                if (next != null) {
                    next.setPrevious(prev);
                } else {
                    // Si el nodo a eliminar es el último, actualizamos la cola
                    tail = prev;
                }

                size--;
                return;
            }
            current = current.getNext();
        }
    }


    @Override
    public int searchElement(T value) {
        Nodo<T> current = this.head;
        int index = 0;
        while (current != null) {
            if (current.getValue().equals(value)) {
                return index;
            }
            current = current.getNext();
            index++;
        }
        return -1;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.head == null;
    }

    @Override
    public String displayList() {
        StringBuilder sb = new StringBuilder();

        Nodo<T> current = this.head;
        while (current != null) {
            sb.append(current.getValue());
            if (current.getNext() != null) {
                sb.append(" <-> ");
            }
            current = current.getNext();
        }

        return sb.toString();
    }

    @Override
    public T get(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        }

        Nodo<T> current = this.head;

        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }

        return current.getValue();
    }

}