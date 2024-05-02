package Estructuras.Lista_Doblemente_Enlazada;


public interface Lista<T> {

    T get(int index) throws IndexOutOfBoundsException;

    void addToFront(T value);

    void addToEnd(T value);

    void insertInMiddle(T value);

    void removeFromFront();

    void removeFromEnd();


    int searchElement(T value);

    int getSize();

    boolean isEmpty();

    String displayList();

	void removeFromMiddle(T value);

}
