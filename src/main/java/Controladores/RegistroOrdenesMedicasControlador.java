package Controladores;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import DAO.GestionOrdenes;
import Estructuras.Lista_Simple.ListaSimple;
import Estructuras.Lista_Simple.Nodo;
import Modelos.OrdenMedica;
import Modelos.Paciente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegistroOrdenesMedicasControlador {
    
    @FXML
    private TextField documentoPacienteTextField;

    @FXML
    private Label nombrePacienteLabel;

    @FXML
    private ComboBox<String> especialidadComboBox;
    
    @FXML
    private ComboBox<String> tipoExamenComboBox;

    @FXML
    private TextField costoExamenTextField;

    @FXML
    private TextArea descripcionTextArea;

    @FXML
    private ComboBox<String> estadoComboBox;

    private ListaSimple<String> tiposExamenPorEspecialidad;


    
    
    @FXML
    public void initialize() {
    	documentoPacienteTextField.textProperty().addListener((observable, oldValue, newValue) -> {buscarPacientePorDocumento();});
    	cargarEspecialidades();
    	especialidadComboBox.setOnAction(event -> cargarTiposExamen());
    	cargarEstados();
    }
    
    private void cargarEspecialidades() {
        especialidadComboBox.getItems().clear();
        tiposExamenPorEspecialidad = new ListaSimple<>();
        String rutaArchivo = "medicos.txt"; 
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(", ");
                if (partes.length >= 4) {
                    String especialidad = partes[3];
                    if (!especialidadComboBox.getItems().contains(especialidad)) {
                        especialidadComboBox.getItems().add(especialidad);
                        tiposExamenPorEspecialidad.append(especialidad);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void cargarTiposExamen() {
        int indiceEspecialidad = especialidadComboBox.getSelectionModel().getSelectedIndex();
        if (indiceEspecialidad >= 0 && indiceEspecialidad < tiposExamenPorEspecialidad.size()) {
            tipoExamenComboBox.getItems().clear();
            String especialidadSeleccionada = tiposExamenPorEspecialidad.getElementoEnPosicion(indiceEspecialidad);
            switch (especialidadSeleccionada) {
            case "Especialista Pediatra":
                tipoExamenComboBox.getItems().addAll("Examen Físico", "Vacunación", "Desarrollo Infantil");
                break;
            case "Especialista Cardiologo":
                tipoExamenComboBox.getItems().addAll("Electrocardiograma (ECG)", "Eco Doppler Cardíaco", "Holter de Ritmo Cardíaco");
                break;
            case "Especialista Dermatologo":
                tipoExamenComboBox.getItems().addAll("Biopsia Cutánea", "Prueba de Alergia Cutánea", "Crioterapia");
                break;
            case "Medicina General":
                tipoExamenComboBox.getItems().addAll("Consulta Médica", "Análisis de Sangre", "Prescripción de Medicamentos");
                break;
            default:
                break;
        }
    }
    }

    
    @FXML
    void buscarPacientePorDocumento() {
        String documento = documentoPacienteTextField.getText();
        Paciente pacienteEncontrado = buscarPacienteEnArchivo(documento);
        if (pacienteEncontrado != null) {
            nombrePacienteLabel.setText(pacienteEncontrado.toString());
        } else {
            nombrePacienteLabel.setText("");
        }
    }

    private Paciente buscarPacienteEnArchivo(String documento) {
        String rutaArchivo = "pacientes.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(", ");
                if (partes.length >= 4 && partes[3].equals(documento)) {
             	   String nombre = partes[0];
                    String apellido = partes[1];
                    int edad = Integer.parseInt(partes[2]);
                    String identificacion = partes[3];
                    String telefono = partes[4];
                    return new Paciente(nombre, apellido, edad, identificacion, telefono);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private void cargarEstados() {
        estadoComboBox.getItems().addAll( "Autorizado", "No Autorizado");
    }
    
    
    

    private boolean pacienteYaTieneExamenDeEspecialidad(Paciente paciente, String especialidad, String tipoExamen) {
        String rutaArchivo = "ordenes_medicas.txt"; // Ruta del archivo donde se almacenan las órdenes médicas
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(", ");
                if (partes.length >= 5 && partes[1].equals(paciente.getIdentificacion()) && partes[3].equals(especialidad) && partes[4].equals(tipoExamen)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private int obtenerProximoIdOrden() {
        String rutaArchivo = "ordenes_medicas.txt";
        int contador = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            while (br.readLine() != null) {
                contador++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Incrementa el contador en uno para obtener el próximo ID disponible
        return contador + 1;
    }


    @FXML
    void registrarOrden(ActionEvent event) throws IOException {
        System.out.println("Iniciando el registro de la orden médica...");

        int idOrden = obtenerProximoIdOrden();
        // Validación de entrada
        if (tipoExamenComboBox.getValue() == null || especialidadComboBox.getValue() == null || costoExamenTextField.getText().isEmpty() || documentoPacienteTextField.getText().isEmpty()) {
            System.out.println("Faltan datos necesarios para registrar la orden.");
            return; // Sale del método si falta algún dato
        }
        double valorExamen = 0;
        try {
            valorExamen = Double.parseDouble(costoExamenTextField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Error en la conversión del costo del examen: " + e.getMessage());
            return; // Sale del método si hay un error en la conversión
        }

        String documentoPaciente = documentoPacienteTextField.getText();
        Paciente paciente = GestionOrdenes.obtenerPacientePorIdentificacion(documentoPaciente);
        if (paciente == null) {
            System.out.println("No se encontró el paciente con documento: " + documentoPaciente);
            return; // Sale del método si no se encuentra el paciente
        }

        // Verificar si el paciente ya tiene un examen registrado para la especialidad y tipo de examen seleccionados
        if (pacienteYaTieneExamenDeEspecialidad(paciente, especialidadComboBox.getValue(), tipoExamenComboBox.getValue())) {
            System.out.println("El paciente ya tiene un examen registrado para esta especialidad y tipo de examen.");
            return; // Sale del método si el paciente ya tiene un examen registrado para la especialidad y tipo de examen seleccionados
        }

        // Creación y guardado de la orden médica
        String descripcion = descripcionTextArea.getText();
        String tipoExamen = tipoExamenComboBox.getValue();
        String especialidad = especialidadComboBox.getValue();
        boolean autorizada = estadoComboBox.getSelectionModel().getSelectedIndex() == 1;

        OrdenMedica orden = new OrdenMedica(idOrden, paciente, tipoExamen, especialidad, descripcion, valorExamen, autorizada);
        guardarOrden(orden);
        cambiarAVistaPaginaPrincipal(event);
        System.out.println("Orden médica registrada con éxito.");
    }


    private void cambiarAVistaPaginaPrincipal(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/estructuradedatos/SistemaGestionClinica/Vistas/pagina_principal.fxml"));
        Parent nuevaRaiz = loader.load();
        Scene nuevaEscena = new Scene(nuevaRaiz);
        stage.setScene(nuevaEscena);
        stage.show();
    }
    
    private void guardarOrden(OrdenMedica orden) {
        String rutaArchivo = "ordenes_medicas.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            String linea = String.format("%d, %s, %s, %s, %s, %.2f, %s\n",
                orden.getId(),
                orden.getPaciente().getIdentificacion(), // Asumiendo que hay un método getIdentificacion en Paciente
                orden.getPaciente().getNombre() + " " + orden.getPaciente().getApellido(), // Concatenar nombre y apellido
                orden.getEspecialidad(),
                orden.getTipoExamen(),
                orden.getValorExamen(),
                orden.isAutorizada() ? "No Autorizada" : "Autorizada");

            writer.write(linea);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public static void actualizarArchivoOrdenes(ListaSimple<OrdenMedica> listaOrdenes) {
		// TODO Auto-generated method stub
		
	}

	public static ListaSimple<OrdenMedica> obtenerListaOrdenes() {
		// TODO Auto-generated method stub
		return null;
	}


}
