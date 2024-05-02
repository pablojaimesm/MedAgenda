package Estructuras.Lista_Circular;

public interface Lista<T> {
    T get(int index) throws IndexOutOfBoundsException;

    void addToFront(T value);

    void addToEnd(T value);

    void insertInMiddle(T value);

    void removeFromFront();

    void removeFromEnd();

    void removeFromMiddle();

    int searchElement(T value);

    int getSize();

    boolean isEmpty();

    String displayList();

    void rotateList(int steps);

    void merge(ListaCircularEnlazada<T> otherList);

    ListaCircularEnlazada<T> splitList(int index);
}
