package Controladores;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import DAO.GestionCitas;
import Estructuras.Lista_Simple.ListaSimple;
import Estructuras.Lista_Simple.Nodo;
import Modelos.Cita;
import Modelos.Medico;
import Modelos.Paciente;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class ListaCitasControlador {

    @FXML
    private ListView<String> citasListView;

    @FXML
    private TextField documentoPacienteTextField;

    @FXML
    private Label nombrePacienteLabel;

    private static final String RUTA_ARCHIVO_CITAS = "citas.txt";

    private ListaSimple<Cita> listaCitas;
    private GestionCitas gestionCitas;

    public void initialize() {
        gestionCitas = new GestionCitas(); 
        documentoPacienteTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                buscarCitasPorDocumento();
            }
        });

        cargarCitasPorDocumento(""); 
        citasListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    System.out.println("Elemento seleccionado: " + newValue);
                }
            }
        });
    }
    
    @FXML
    void buscarCitasPorDocumento() {
        String documento = documentoPacienteTextField.getText();
        listaCitas = cargarCitasPorDocumento(documento);
        if (!listaCitas.isEmpty()) {
            citasListView.getItems().clear();
            Nodo<Cita> nodoActual = listaCitas.getHead();
            while (nodoActual != null) {
                citasListView.getItems().add(nodoActual.getValue().toString());
                nodoActual = nodoActual.getNext();
            }
        } else {
            citasListView.getItems().clear();
            citasListView.getItems().add("No se encontraron citas para el paciente con documento " + documento);
        }
    }

    private ListaSimple<Cita> cargarCitasPorDocumento(String documento) {
        ListaSimple<Cita> citasDelPaciente = new ListaSimple<>();
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_ARCHIVO_CITAS))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(", ");
                String pacienteIdentificacion = partes[4];
                if (pacienteIdentificacion.equals(documento)) {
                    String idCita = partes[0];
                    boolean citaExistente = false;
                    Nodo<Cita> nodoActual = citasDelPaciente.getHead();
                    while (nodoActual != null) {
                        if (nodoActual.getValue().getId().equals(idCita)) {
                            citaExistente = true;
                            break;
                        }
                        nodoActual = nodoActual.getNext();
                    }
                    if (!citaExistente) {
                        String fecha = partes[1];
                        String hora = partes[2];
                        String especialidad = partes[5];
                        String motivo = partes[6];
                        String ticket = partes[7];
                        double valorConsulta = Double.parseDouble(partes[8]);
                        boolean pagado = partes[9].equals("Pagado");
                        Medico medicoAsignado = obtenerMedicoPorId(partes[3]);
                        Paciente paciente = obtenerPacientePorIdentificacion(pacienteIdentificacion);
                        Cita cita = new Cita(idCita, fecha, hora, medicoAsignado, paciente, especialidad, motivo, ticket, valorConsulta, pagado);
                        insertarOrdenado(citasDelPaciente, cita);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return citasDelPaciente;
    }
    
    private Medico obtenerMedicoPorId(String id) {
        Medico medico = null;
        medico = gestionCitas.obtenerMedicoPorId(id);
        return medico;
    }

    private Paciente obtenerPacientePorIdentificacion(String identificacion) {
        Paciente paciente = null;
        paciente = gestionCitas.obtenerPacientePorIdentificacion(identificacion);
        return paciente;
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
	public void cancelarCita() {
	    int indiceSeleccionado = citasListView.getSelectionModel().getSelectedIndex();
	    if (indiceSeleccionado >= 0 && indiceSeleccionado < listaCitas.size()) {
	        Cita citaAEliminar = listaCitas.getElementoEnPosicion(indiceSeleccionado);
	        System.out.println("Cita a eliminar: " + citaAEliminar.toString());
	        citasListView.getItems().remove(indiceSeleccionado);
	        GestionCitas gestionCitas = new GestionCitas();
	        gestionCitas.eliminarCitaDelArchivo(citaAEliminar.getId(), "citas.txt");
	    } else {
	        System.out.println("No se ha seleccionado ninguna cita para cancelar o la selección es inválida.");
	    }
	}


	private void insertarOrdenado(ListaSimple<Cita> lista, Cita nuevaCita) {
	    Nodo<Cita> nuevoNodo = new Nodo<>(nuevaCita);
	    if (lista.isEmpty()) {
	        lista.append(nuevaCita);
	        return;
	    }
	    Nodo<Cita> actual = lista.getHead();
	    Nodo<Cita> anterior = null;
	    while (actual != null) {
	        int comparacionFecha = actual.getValue().getFecha().compareTo(nuevaCita.getFecha());
	        if (comparacionFecha == 0) {
	            int comparacionHora = actual.getValue().getHora().compareTo(nuevaCita.getHora());
	            if (comparacionHora == 0) {
	                int comparacionID = actual.getValue().getId().compareTo(nuevaCita.getId());
	                if (comparacionID > 0) {
	                    nuevoNodo.setNext(actual);
	                    if (anterior == null) {
	                        lista.setHead(nuevoNodo);
	                    } else {
	                        anterior.setNext(nuevoNodo);
	                    }
	                    return;
	                }
	            } else if (comparacionHora > 0) {
	                nuevoNodo.setNext(actual);
	                if (anterior == null) {
	                    lista.setHead(nuevoNodo);
	                } else {
	                    anterior.setNext(nuevoNodo);
	                }
	                return;
	            }
	        } else if (comparacionFecha > 0) {
	            nuevoNodo.setNext(actual);
	            if (anterior == null) {
	                lista.setHead(nuevoNodo);
	            } else {
	                anterior.setNext(nuevoNodo);
	            }
	            return;
	        }
	        anterior = actual;
	        actual = actual.getNext();
	    }
	    anterior.setNext(nuevoNodo);
	}

}