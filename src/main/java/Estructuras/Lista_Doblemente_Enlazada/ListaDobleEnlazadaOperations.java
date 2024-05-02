package Estructuras.Lista_Doblemente_Enlazada;

public interface ListaDobleEnlazadaOperations<T> {
    void addToFront(T value);
    void addToEnd(T value);
    void insertInMiddle(T value);
    void removeFromFront();
    void removeFromEnd();
    void removeFromMiddle(T value);
    int searchElement(T value);
    int getSize();
    boolean isEmpty();
    String displayList();
    T get(int index) throws IndexOutOfBoundsException;
}
