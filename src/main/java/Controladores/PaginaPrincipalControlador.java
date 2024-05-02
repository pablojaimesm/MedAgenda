package Controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;

public class PaginaPrincipalControlador {

    @FXML
    private MenuItem gestionCitasButton;

    @FXML
    private MenuItem taquillaPagosButton;

    @FXML
    private MenuItem llamadoPacientesButton;

    @FXML
    private MenuItem registroOrdenesMedicasButton;

    @FXML
    private MenuItem autorizacionExamenesButton;

    @FXML
    private MenuItem listaCitasButton;

    @FXML
    private MenuItem registrarPagosButton;

    @FXML
    private void navegarProgramarCitas(ActionEvent event) throws IOException {
        cargarNuevaEscena("/com/estructuradedatos/SistemaGestionClinica/Vistas/programar_citas.fxml", event);
    }

    @FXML
    private void navegarRegistrarPacientes(ActionEvent event) throws IOException {
        cargarNuevaEscena("/com/estructuradedatos/SistemaGestionClinica/Vistas/registro_pacientes.fxml", event);
    }

    @FXML
    private void navegarLlamadoPacientes(ActionEvent event) throws IOException {
        cargarNuevaEscena("/com/estructuradedatos/SistemaGestionClinica/Vistas/llamado_pacientes.fxml", event);
    }

    @FXML
    private void navegarRegistroOrdenesMedicas(ActionEvent event) throws IOException {
        cargarNuevaEscena("/com/estructuradedatos/SistemaGestionClinica/Vistas/registro_ordenes_medicas.fxml", event);
    }

    @FXML
    private void navegarAutorizacionExamenes(ActionEvent event) throws IOException {
        cargarNuevaEscena("/com/estructuradedatos/SistemaGestionClinica/Vistas/autorizacion_examenes.fxml", event);
    }

    @FXML
    private void navegarListaCitas(ActionEvent event) throws IOException {
        cargarNuevaEscena("/com/estructuradedatos/SistemaGestionClinica/Vistas/lista_citas.fxml", event);
    }

    @FXML
    private void navegarTaquilla(ActionEvent event) throws IOException {
    	cargarNuevaEscena("/com/estructuradedatos/SistemaGestionClinica/Vistas/taquilla.fxml", event);
    }
    

    private void cargarNuevaEscena(String nombreVista, ActionEvent event) throws IOException {
        MenuItem menuItem = (MenuItem) event.getSource();
        Stage ventanaPrincipal = (Stage) menuItem.getParentPopup().getOwnerWindow();
        
        if (ventanaPrincipal != null) {
            Scene nuevaEscena = new Scene(FXMLLoader.load(getClass().getResource(nombreVista)));
            ventanaPrincipal.setScene(nuevaEscena);
            ventanaPrincipal.show();
        } else {
            System.err.println("Error: La ventana principal es nula.");
        }
    }


}

