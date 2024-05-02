package Estructuras.Pila;

//Importar la clase Arrays para manejar operaciones con arreglos
import java.util.Arrays;

//Definir la clase de la Pila parametrizada por tipo (generics)
public class Pila<T> {
 // Definir la capacidad inicial de la pila
 private static final int CAPACIDAD_INICIAL = 10;

 // Atributos de la clase
 private Object[] elementos; // Arreglo para almacenar los elementos de la pila
 private int tamaño; // Tamaño actual de la pila

 // Constructor de la clase
 public Pila() {
     this.elementos = new Object[CAPACIDAD_INICIAL]; // Inicializar el arreglo con la capacidad inicial
     this.tamaño = 0; // Inicializar el tamaño como cero
 }

 // Método para agregar un elemento a la pila
 public void push(T elemento) {
     asegurarCapacidad(); // Asegurar que haya espacio suficiente en la pila
     elementos[tamaño++] = elemento; // Agregar el elemento al final de la pila y actualizar el tamaño
 }

 // Método para remover y devolver el elemento en la cima de la pila
 public T pop() {
     verificarNoVacia(); // Verificar si la pila no está vacía
     T elemento = obtenerElemento(--tamaño); // Obtener el elemento en la cima y actualizar el tamaño
     elementos[tamaño] = null; // Permitir la recolección de basura
     return elemento; // Devolver el elemento obtenido
 }

 // Método para obtener el elemento en la cima de la pila sin removerlo
 public T peek() {
     verificarNoVacia(); // Verificar si la pila no está vacía
     return obtenerElemento(tamaño - 1); // Devolver el elemento en la cima
 }

 // Método para verificar si la pila está vacía
 public boolean estaVacia() {
     return tamaño == 0; // La pila está vacía si su tamaño es cero
 }

 // Método para obtener el tamaño actual de la pila
 public int getSize() {
     return tamaño; // Devolver el tamaño actual de la pila
 }

 // Método privado para asegurar que haya capacidad suficiente en la pila
 private void asegurarCapacidad() {
     if (tamaño == elementos.length) {
         elementos = aumentarCapacidad(); // Duplicar la capacidad de la pila si está llena
     }
 }

 // Método privado para duplicar la capacidad de la pila
 private Object[] aumentarCapacidad() {
     int nuevaCapacidad = tamaño * 2; // Calcular la nueva capacidad como el doble del tamaño actual
     return Arrays.copyOf(elementos, nuevaCapacidad); // Crear un nuevo arreglo con la nueva capacidad
 }

 // Método privado para obtener un elemento en un índice dado
 private T obtenerElemento(int índice) {
     return (T) elementos[índice]; // Convertir y devolver el elemento en el índice especificado
 }

 // Método privado para verificar si la pila no está vacía
 private void verificarNoVacia() {
     if (estaVacia()) {
         throw new IllegalStateException("La pila está vacía"); // Lanzar una excepción si la pila está vacía
     }
 }
}

