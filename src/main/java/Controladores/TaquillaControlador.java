package Controladores;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import DAO.ColaEsperaEspecialidad;
import DAO.GestionCitas;
import Estructuras.Cola.Cola;
import Modelos.Cita;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TaquillaControlador {

    @FXML
    private TextField idCitaTextField;
    @FXML
    private Label citaLabel;
    private GestionCitas gestionCitas;

    private ColaEsperaEspecialidad colaPediatra;
    private ColaEsperaEspecialidad colaCardiologo;
    private ColaEsperaEspecialidad colaDermatologo;
    private ColaEsperaEspecialidad colaMedicinaGeneral;

    public void initialize() {
        setGestionCitas(new GestionCitas());
        cargarColasDesdeArchivo();
        idCitaTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                mostrarCita(newValue);
            }
        });
    }
    
    
    private void cargarColasDesdeArchivo() {
        colaPediatra = new ColaEsperaEspecialidad("Especialista Pediatra");
        colaCardiologo = new ColaEsperaEspecialidad("Especialista Cardiologo");
        colaDermatologo = new ColaEsperaEspecialidad("Especialista Dermatologo");
        colaMedicinaGeneral = new ColaEsperaEspecialidad("Medicina General");
        if (Files.exists(Paths.get("Especialista Pediatra.txt"))) {
            colaPediatra.cargarColaDesdeArchivo();
        }
        if (Files.exists(Paths.get("Especialista Cardiologo.txt"))) {
            colaCardiologo.cargarColaDesdeArchivo();
        }
        if (Files.exists(Paths.get("Especialista Dermatologo.txt"))) {
            colaDermatologo.cargarColaDesdeArchivo();
        }
        if (Files.exists(Paths.get("Medicina General.txt"))) {
            colaMedicinaGeneral.cargarColaDesdeArchivo();
        }
    }

    
    private void mostrarCita(String ticket) {
        if (GestionCitas.getListaCitas() != null) {
            Cita cita = GestionCitas.buscarCitaPorTicket(ticket);
            if (cita != null) {
                if (cita.isPagado()) {
                    citaLabel.setText("La cita con el ticket " + ticket + " ya ha sido pagada.");
                } else {
                    citaLabel.setText(cita.toString());
                }
            } else {
                System.out.println("Cita no encontrada para ticket: " + ticket);
            }
        } else {
            System.out.println("Error: listaCitas no ha sido inicializada correctamente.");
        }
    }


    @FXML
    void registrarPago(ActionEvent event) throws IOException {
        String ticket = idCitaTextField.getText();
        if (GestionCitas.getListaCitas() != null) {
            Cita cita = GestionCitas.buscarCitaPorTicket(ticket);
            if (cita != null) {
                if (cita.isPagado()) {
                    cambiarAVistaPaginaPrincipal(event);
                } else {
                    GestionCitas.marcarComoPagada(ticket);
                    if (cita.getEspecialidad().equals("Especialista Pediatra")) {
                        colaPediatra.agregarCitaPagada(cita.getId());
                        System.out.println("Pago registrado para cita con ticket: " + ticket);
                        System.out.println("Contenido de la cola de la especialidad Especialista Pediatra:");
                        System.out.println(colaPediatra.getIdsCitasPagadas().mostrarCola());
                        colaPediatra.guardarColaEnArchivo(); 
                        cambiarAVistaPaginaPrincipal(event);
                    } else if (cita.getEspecialidad().equals("Especialista Cardiologo")) {
                        colaCardiologo.agregarCitaPagada(cita.getId());
                        System.out.println("Pago registrado para cita con ticket: " + ticket);
                        System.out.println("Contenido de la cola de la especialidad Especialista Cardiologo:");
                        System.out.println(colaCardiologo.getIdsCitasPagadas().mostrarCola());
                        colaCardiologo.guardarColaEnArchivo();
                        cambiarAVistaPaginaPrincipal(event);
                    } else if (cita.getEspecialidad().equals("Especialista Dermatologo")) {
                        colaDermatologo.agregarCitaPagada(cita.getId());
                        System.out.println("Pago registrado para cita con ticket: " + ticket);
                        System.out.println("Contenido de la cola de la especialidad Especialista Dermatologo:");
                        System.out.println(colaDermatologo.getIdsCitasPagadas().mostrarCola());
                        colaDermatologo.guardarColaEnArchivo(); 
                        cambiarAVistaPaginaPrincipal(event);
                    } else if (cita.getEspecialidad().equals("Medicina General")) {
                        colaMedicinaGeneral.agregarCitaPagada(cita.getId());
                        System.out.println("Pago registrado para cita con ticket: " + ticket);
                        System.out.println("Contenido de la cola de la especialidad Medicina General:");
                        System.out.println(colaMedicinaGeneral.getIdsCitasPagadas().mostrarCola());
                        colaMedicinaGeneral.guardarColaEnArchivo(); 
                        cambiarAVistaPaginaPrincipal(event);
                    } else {
                        System.out.println("Especialidad desconocida: " + cita.getEspecialidad());
                    }
                }
                
            } else {
                System.out.println("Cita no encontrada para ticket: " + ticket);
                cambiarAVistaPaginaPrincipal(event);
            }
        } else {
            System.out.println("Error: listaCitas no ha sido inicializada correctamente.");
        }
    }

    
    
    private void imprimirColasPorEspecialidad() {
        System.out.println("Contenido de las colas por especialidad:");
        System.out.println("Cola de la especialidad Especialista Pediatra:");
        System.out.println(colaPediatra.getIdsCitasPagadas().mostrarCola());
        System.out.println("Cola de la especialidad Especialista Cardiologo:");
        System.out.println(colaCardiologo.getIdsCitasPagadas().mostrarCola());
        System.out.println("Cola de la especialidad Especialista Dermatologo:");
        System.out.println(colaDermatologo.getIdsCitasPagadas().mostrarCola());
        System.out.println("Cola de la especialidad Medicina General:");
        System.out.println(colaMedicinaGeneral.getIdsCitasPagadas().mostrarCola());
    }


    
    private void cambiarAVistaPaginaPrincipal(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/estructuradedatos/SistemaGestionClinica/Vistas/pagina_principal.fxml"));
        Parent nuevaRaiz = loader.load();
        Scene nuevaEscena = new Scene(nuevaRaiz);
        stage.setScene(nuevaEscena);
        stage.show();
    }


	public GestionCitas getGestionCitas() {
		return gestionCitas;
	}


	public void setGestionCitas(GestionCitas gestionCitas) {
		this.gestionCitas = gestionCitas;
	}
}
