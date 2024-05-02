package Controladores;

import java.io.IOException;

import DAO.PacienteDAO;
import Modelos.Paciente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegistroPacientesControlador {

    @FXML
    private TextField nombresTextField;

    @FXML
    private TextField apellidosTextField;

    @FXML
    private TextField edadTextField;

    @FXML
    private TextField identificacionTextField;

    @FXML
    private TextField telefonoTextField;

    @FXML
    private Label mensajeLabel;

    private PacienteDAO pacienteDAO;

    public RegistroPacientesControlador() {
        pacienteDAO = new PacienteDAO();
    }

    @FXML
    public void registrarPaciente(ActionEvent event) throws IOException {
        if (nombresTextField.getText().isEmpty() || apellidosTextField.getText().isEmpty() ||
                edadTextField.getText().isEmpty() || identificacionTextField.getText().isEmpty() ||
                telefonoTextField.getText().isEmpty()) {
            mensajeLabel.setText("Por favor, rellene todos los campos.");
            return;
        }
        String nombre = nombresTextField.getText();
        String apellido = apellidosTextField.getText();
        int edad = Integer.parseInt(edadTextField.getText());
        String identificacion = identificacionTextField.getText();
        String telefono = telefonoTextField.getText();
        if (pacienteDAO.existePacienteConIdentificacion(identificacion)) {
            mensajeLabel.setText("Ya existe un paciente con este número de identificación.");
            return;
        }
        pacienteDAO.agregarPaciente(new Paciente(nombre, apellido, edad, identificacion, telefono));
        limpiarCampos();
        cambiarAVistaPaginaPrincipal(event);
    }
    
    private void cambiarAVistaPaginaPrincipal(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/estructuradedatos/SistemaGestionClinica/Vistas/pagina_principal.fxml"));
        Parent nuevaRaiz = loader.load();
        Scene nuevaEscena = new Scene(nuevaRaiz);
        stage.setScene(nuevaEscena);
        stage.show();
    }

    private void limpiarCampos() {
        nombresTextField.clear();
        apellidosTextField.clear();
        edadTextField.clear();
        identificacionTextField.clear();
        telefonoTextField.clear();
        mensajeLabel.setText("");
    }
}
