package Estructuras.Lista_Simple;

import java.util.Comparator;

public interface Lista<T extends Comparable<T>> extends Comparable<Lista<T>> {

    void append(T valor); // Agrega un valor al final de la lista

    void prepend(T valor); // Agrega un valor al principio de la lista

    void insert(T valor, int indice); // Inserta un valor en un índice específico

    T get_element_at(int indice); // Obtiene el elemento en una posición específica

    int size(); // Retorna el número de nodos en la lista

    void sort(); // Ordena la lista

    Lista<T> filter(java.util.function.Predicate<T> predicate); // Filtra elementos por condición

    void removeDuplicates(); // Elimina elementos duplicados

	void sort(Comparator<T> comparator);

	void remove(T elemento);
}