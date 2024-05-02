package Controladores;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import Estructuras.Lista_Simple.ListaSimple;
import Estructuras.Lista_Simple.Nodo;
import Modelos.OrdenMedica;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ListaOrdenesMedicasControlador {

    @FXML
    private TextField documentoPacienteTextField;

    @FXML
    private ListView<String> ordenesListView;

    private ListaSimple<String> ordenesMedicas;

    private static ListaSimple<OrdenMedica> listaOrdenes;

    private static final String RUTA_ARCHIVO_ORDENES_MEDICAS = "ordenes_medicas.txt";

    public void initialize() {
        documentoPacienteTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            actualizarOrdenesMedicas();
        });

        cargarOrdenesPorDocumento("");
    }

    private void cargarOrdenesPorDocumento(String documento) {
        ordenesMedicas = new ListaSimple<>();
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_ARCHIVO_ORDENES_MEDICAS))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(", ");
                if (partes.length >= 2 && partes[1].equals(documento)) {
                    ordenesMedicas.append(linea);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        actualizarListaOrdenes();
    }

    private void actualizarListaOrdenes() {
        ordenesListView.getItems().clear();
        if (ordenesMedicas != null) {
            Nodo<String> nodoActual = ordenesMedicas.getHead();
            while (nodoActual != null) {
                ordenesListView.getItems().add(nodoActual.getValue());
                nodoActual = nodoActual.getNext();
            }
        }
    }

    private void cambiarAVistaPaginaPrincipal(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/estructuradedatos/SistemaGestionClinica/Vistas/pagina_principal.fxml"));
        Parent nuevaRaiz = loader.load();
        Scene nuevaEscena = new Scene(nuevaRaiz);
        stage.setScene(nuevaEscena);
        stage.show();
    }
    
    @FXML
    public void regresar(ActionEvent event) throws IOException{
        cambiarAVistaPaginaPrincipal(event);
    }

    @FXML
    public void cancelarOrden() {
        int indiceSeleccionado = ordenesListView.getSelectionModel().getSelectedIndex();
        if (indiceSeleccionado >= 0 && indiceSeleccionado < ordenesListView.getItems().size()) {
            String orden = ordenesListView.getItems().get(indiceSeleccionado);
            String[] partes = orden.split(", ");
            String estado = partes[6];
            if (estado.equals("No Autorizada")) {
                ordenesListView.getItems().remove(indiceSeleccionado);
            } else {
                System.out.println("No se puede cancelar una orden médica autorizada.");
            }
        } else {
            System.out.println("No se ha seleccionado ninguna orden médica para cancelar o la selección es inválida.");
        }
    }

    @FXML
    void autorizarOrden(ActionEvent event) {
        int indiceSeleccionado = ordenesListView.getSelectionModel().getSelectedIndex();
        if (indiceSeleccionado >= 0 && indiceSeleccionado < ordenesListView.getItems().size()) {
            String orden = ordenesListView.getItems().get(indiceSeleccionado);
            System.out.println("Orden seleccionada: " + orden);
            String[] partes = orden.split(", ");
            if (partes.length >= 7) {
                String estado = partes[6];
                System.out.println("Estado de la orden: " + estado);
                if (estado.equals("No Autorizada")) {
                    partes[6] = "Autorizada";
                    String ordenActualizada = String.join(", ", partes);
                    ordenesListView.getItems().set(indiceSeleccionado, ordenActualizada);

                    // Actualizar el estado de la orden en el archivo
                    actualizarEstadoOrdenEnArchivo(partes[0], "Autorizada");
                    System.out.println("La orden médica ha sido autorizada en el archivo.");
                } else {
                    System.out.println("La orden médica ya está autorizada.");
                }
            } else {
                System.out.println("La orden seleccionada no tiene el formato correcto.");
            }
        } else {
            System.out.println("No se ha seleccionado ninguna orden médica para autorizar o la selección es inválida.");
        }
    }

    private void actualizarEstadoOrdenEnArchivo(String idOrden, String nuevoEstado) {
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_ARCHIVO_ORDENES_MEDICAS));
             BufferedWriter bw = new BufferedWriter(new FileWriter("ordenes_medicas_temp.txt"))) {

            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(", ");
                if (partes.length >= 1 && partes[0].equals(idOrden)) {
                    bw.write(linea.replace("No Autorizada", nuevoEstado));
                } else {
                    bw.write(linea);
                }
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Renombrar el archivo temporal al archivo original
        try {
            Files.move(Paths.get("ordenes_medicas_temp.txt"), Paths.get(RUTA_ARCHIVO_ORDENES_MEDICAS), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private void actualizarOrdenesMedicas() {
        String documento = documentoPacienteTextField.getText();
        cargarOrdenesPorDocumento(documento);
    }

    


}
