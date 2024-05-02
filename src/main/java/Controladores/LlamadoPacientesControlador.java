package Controladores;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;

import DAO.ColaEsperaEspecialidad;
import DAO.GestionCitas;
import Estructuras.Lista_Circular.ListaCircularEnlazada;
import Estructuras.Lista_Simple.ListaSimple;
import Modelos.Cita;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class LlamadoPacientesControlador {

    @FXML
    private Label pediatraLabel;

    @FXML
    private Label cardiologoLabel;

    @FXML
    private Label dermatologoLabel;

    @FXML
    private Label medicinaGeneralLabel;

    private ColaEsperaEspecialidad colaPediatra;
    private ColaEsperaEspecialidad colaCardiologo;
    private ColaEsperaEspecialidad colaDermatologo;
    private ColaEsperaEspecialidad colaMedicinaGeneral;
    private GestionCitas gestionCitas;

    public void initialize() {
        gestionCitas = new GestionCitas();
        colaPediatra = new ColaEsperaEspecialidad("Pediatra");
        colaCardiologo = new ColaEsperaEspecialidad("Cardiologo");
        colaDermatologo = new ColaEsperaEspecialidad("Dermatologo");
        colaMedicinaGeneral = new ColaEsperaEspecialidad("Medicina General");
        System.out.println("Colas de espera creadas.");
        setColas(colaPediatra, colaCardiologo, colaDermatologo, colaMedicinaGeneral);
        cargarCitasPagadas();
        if (colaPediatra.getIdsCitasPagadas().estaVacia() &&
            colaCardiologo.getIdsCitasPagadas().estaVacia() &&
            colaDermatologo.getIdsCitasPagadas().estaVacia() &&
            colaMedicinaGeneral.getIdsCitasPagadas().estaVacia()) {
            pediatraLabel.setText("No hay citas disponibles");
            cardiologoLabel.setText("No hay citas disponibles");
            dermatologoLabel.setText("No hay citas disponibles");
            medicinaGeneralLabel.setText("No hay citas disponibles");
        } else {
            actualizarProximasCitas();
        }
    }


    public void setColas(ColaEsperaEspecialidad pediatra, ColaEsperaEspecialidad cardiologo,
                         ColaEsperaEspecialidad dermatologo, ColaEsperaEspecialidad medicinaGeneral) {
        this.colaPediatra = pediatra;
        this.colaCardiologo = cardiologo;
        this.colaDermatologo = dermatologo;
        this.colaMedicinaGeneral = medicinaGeneral;
        System.out.println("Colas de espera configuradas.");
        actualizarProximasCitas();
    }

    private ListaCircularEnlazada<Cita> cargarCitas() {
        ListaCircularEnlazada<Cita> citas = new ListaCircularEnlazada<>();
        String rutaArchivo = "citas.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String ticket = linea.trim();
                Cita cita = GestionCitas.buscarCitaPorTicket(ticket);
                if (cita != null) {
                    citas.addToEnd(cita);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return citas;
    }

    private void cargarCitasPagadas() {
        ListaCircularEnlazada<Cita> citasDisponibles = cargarCitas();
        cargarCitasPagadas("Especialista Pediatra.txt", colaPediatra, citasDisponibles);
        cargarCitasPagadas("Especialista Cardiologo.txt", colaCardiologo, citasDisponibles);
        cargarCitasPagadas("Especialista Dermatologo.txt", colaDermatologo, citasDisponibles);
        cargarCitasPagadas("Medicina General.txt", colaMedicinaGeneral, citasDisponibles);
    }

    private void cargarCitasPagadas(String nombreArchivo, ColaEsperaEspecialidad colaEspecialidad, ListaCircularEnlazada<Cita> citasDisponibles) {
        String rutaArchivo = nombreArchivo;
        System.out.println("Intentando cargar citas del archivo: " + rutaArchivo); 
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println("Línea leída del archivo: " + linea);
                String idCita = linea.trim();
                Cita cita = GestionCitas.buscarCitaPorId(idCita);
                if (cita != null) {
                    System.out.println("Cita encontrada para el ID " + idCita + ": " + cita.toString());
                    String ticket = cita.getTicket();
                    colaEspecialidad.getIdsCitasPagadas().enqueue(ticket);
                } else {
                    System.out.println("No se encontró ninguna cita para el ID " + idCita);
                }
            }
            System.out.println("Contenido de la cola de espera para " + colaEspecialidad.getEspecialidad() + ": ");
            colaEspecialidad.getIdsCitasPagadas().displayCola();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void actualizarProximasCitas() {
        String ticketPediatra = colaPediatra.getIdsCitasPagadas().peek();
        String ticketCardiologo = colaCardiologo.getIdsCitasPagadas().peek();
        String ticketDermatologo = colaDermatologo.getIdsCitasPagadas().peek();
        String ticketMedicinaGeneral = colaMedicinaGeneral.getIdsCitasPagadas().peek();
        pediatraLabel.setText(ticketPediatra != null ? ticketPediatra : "No hay citas disponibles");
        cardiologoLabel.setText(ticketCardiologo != null ? ticketCardiologo : "No hay citas disponibles");
        dermatologoLabel.setText(ticketDermatologo != null ? ticketDermatologo : "No hay citas disponibles");
        medicinaGeneralLabel.setText(ticketMedicinaGeneral != null ? ticketMedicinaGeneral : "No hay citas disponibles");
    }
    
    private Cita obtenerCitaDesdeTicket(String ticket) {
        return GestionCitas.buscarCitaPorTicket(ticket);
    }

    @FXML
    void avanzarPediatra(ActionEvent event) {
        String ticketPediatra = colaPediatra.getIdsCitasPagadas().dequeue();
        if (ticketPediatra != null) {
            System.out.println("Ticket eliminado: " + ticketPediatra); 
            Cita cita = obtenerCitaDesdeTicket(ticketPediatra);
            if (cita != null) {
                String idCita = cita.getId();
                eliminarCitaDeArchivo("Especialista Pediatra.txt", idCita);
                actualizarProximasCitas();
            } else {
                System.out.println("No se encontró ninguna cita para el ticket: " + ticketPediatra);
            }
        }
    }

    @FXML
    void avanzarCardiologo(ActionEvent event) {
        String ticketCardiologo = colaCardiologo.getIdsCitasPagadas().dequeue();
        if (ticketCardiologo != null) {
            System.out.println("Ticket eliminado: " + ticketCardiologo);
            Cita cita = obtenerCitaDesdeTicket(ticketCardiologo);
            if (cita != null) {
                String idCita = cita.getId();
                eliminarCitaDeArchivo("Especialista Cardiologo.txt", idCita);
                actualizarProximasCitas();
            } else {
                System.out.println("No se encontró ninguna cita para el ticket: " + ticketCardiologo);
            }
        }
    }

    @FXML
    void avanzarDermatologo(ActionEvent event) {
        String ticketDermatologo = colaDermatologo.getIdsCitasPagadas().dequeue();
        if (ticketDermatologo != null) {
            System.out.println("Ticket eliminado: " + ticketDermatologo);
            Cita cita = obtenerCitaDesdeTicket(ticketDermatologo);
            if (cita != null) {
                String idCita = cita.getId();
                eliminarCitaDeArchivo("Especialista Dermatologo.txt", idCita);
                actualizarProximasCitas();
            } else {
                System.out.println("No se encontró ninguna cita para el ticket: " + ticketDermatologo);
            }
        }
    }

    @FXML
    void avanzarMedicinaGeneral(ActionEvent event) {
        String ticketMedicinaGeneral = colaMedicinaGeneral.getIdsCitasPagadas().dequeue();
        if (ticketMedicinaGeneral != null) {
            System.out.println("Ticket eliminado: " + ticketMedicinaGeneral);
            Cita cita = obtenerCitaDesdeTicket(ticketMedicinaGeneral);
            if (cita != null) {
                String idCita = cita.getId();
                eliminarCitaDeArchivo("Medicina General.txt", idCita);
                actualizarProximasCitas();
            } else {
                System.out.println("No se encontró ninguna cita para el ticket: " + ticketMedicinaGeneral);
            }
        }
    }
    
    private void eliminarCitaDeArchivo(String nombreArchivo, String idCita) {
        File archivo = new File(nombreArchivo);
        ListaSimple<String> lineas = new ListaSimple<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().startsWith(idCita)) {
                    lineas.append(linea);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, false))) {
            for (String linea : lineas) {
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
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
    

}
