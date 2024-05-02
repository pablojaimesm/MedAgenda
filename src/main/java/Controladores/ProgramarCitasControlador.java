package Controladores;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import DAO.GestionCitas;
import Estructuras.Lista_Simple.ListaSimple;
import Estructuras.Lista_Simple.Nodo;
import Modelos.Cita;
import Modelos.Medico;
import Modelos.Paciente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;


public class ProgramarCitasControlador {

	@FXML
    private TextField documentoPacienteTextField;
    @FXML
    private Label nombrePacienteLabel;
    @FXML
    private DatePicker fechaDatePicker;
    @FXML
    private ComboBox<LocalTime> horaComboBox;
    @FXML
    private ComboBox<String> especialidadComboBox;
    @FXML
    private ComboBox<Medico> profesionalComboBox;
    @FXML
    private TextArea motivoTextArea;

    private GestionCitas gestionCitas;

    public ProgramarCitasControlador() {
        gestionCitas = new GestionCitas();
    }

    @FXML
    public void initialize() {
    	documentoPacienteTextField.textProperty().addListener((observable, oldValue, newValue) -> {buscarPacientePorDocumento();});
    	cargarEspecialidades();
    	 especialidadComboBox.setOnAction(event -> cargarMedicosPorEspecialidad());
    	 horaComboBox();
    }
    
    
    
    private void cargarEspecialidades() {
        especialidadComboBox.getItems().clear();
        String rutaArchivo = "medicos.txt"; 
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(", ");
                if (partes.length >= 4) {
                    String especialidad = partes[3];
                    if (!especialidadComboBox.getItems().contains(especialidad)) {
                        especialidadComboBox.getItems().add(especialidad);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarMedicosPorEspecialidad() {
        String especialidadSeleccionada = especialidadComboBox.getValue();
        if (especialidadSeleccionada != null) {
            cargarMedicosDesdeArchivo(especialidadSeleccionada);
        }
    }

    private void cargarMedicosDesdeArchivo(String especialidad) {
        profesionalComboBox.getItems().clear();
        String rutaArchivo = "medicos.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(", ");
                if (partes.length >= 4 && partes[3].equals(especialidad)) {
                    String id = partes[0];
                    String nombre = partes[1];
                    String apellido = partes[2];
                    Medico medico = new Medico(id, nombre, apellido, especialidad);
                    profesionalComboBox.getItems().add(medico);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
   void horaComboBox() {
        ListaSimple<LocalTime> horasDisponibles = new ListaSimple<>();
        LocalTime inicio = LocalTime.of(8, 0);
        LocalTime fin = LocalTime.of(17, 0);
        while (inicio.isBefore(fin.plusSeconds(1))) { 
            horasDisponibles.append(inicio);
            inicio = inicio.plusMinutes(30);
        }
        Nodo<LocalTime> nodoActual = horasDisponibles.getHead();
        while (nodoActual != null) {
            horaComboBox.getItems().add(nodoActual.getValue());
            nodoActual = nodoActual.getNext();
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
    
    
    


   @FXML
   public void confirmarCita(ActionEvent event) throws IOException {
       String especialidad = especialidadComboBox.getValue();
       Medico medico = profesionalComboBox.getValue();
       String motivo = motivoTextArea.getText();
       String documentoPaciente = documentoPacienteTextField.getText();
       LocalDate fecha = fechaDatePicker.getValue(); 
       LocalTime hora = horaComboBox.getValue();
       System.out.println("Especialidad seleccionada: " + especialidad);
       System.out.println("Médico seleccionado: " + medico.toString());
       System.out.println("Motivo: " + motivo);
       System.out.println("Documento del paciente: " + documentoPaciente);
       System.out.println("Fecha seleccionada: " + fecha.toString());
       System.out.println("Hora seleccionada: " + hora.toString());
       Paciente paciente = gestionCitas.obtenerPacientePorIdentificacion(documentoPaciente);
       if (paciente != null) {
           Medico medicoEncontrado = gestionCitas.obtenerMedicoPorId(medico.getId());
           if (medicoEncontrado != null) {
               Cita cita = gestionCitas.registrarCita(especialidad, motivo, medicoEncontrado, paciente, fecha, hora);
               if (cita != null) {
                   gestionCitas.guardarCitasEnArchivo();
                   cambiarAVistaPaginaPrincipal(event);
               } else {
                   System.out.println("Error al registrar la cita.");
               }
           } else {
               System.out.println("No se encontró al médico con el ID especificado.");
           }
       } else {
           System.out.println("No se encontró al paciente con el número de documento especificado.");
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

}