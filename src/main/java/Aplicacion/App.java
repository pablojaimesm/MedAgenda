package Aplicacion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
    	Parent root = FXMLLoader.load(getClass().getResource("/com/estructuradedatos/SistemaGestionClinica/Vistas/pagina_principal.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Sistema de Gestión Clínica");
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
