package mx.uam.ayd.proyecto.presentacion.pacientePrincipal;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class VentanaPacientePrincipal {

    private Stage stage;
    private ControlPaciente controlador;
    private boolean initialized = false;

    /**
     * Inicializa la interfaz de usuario
     */
    private void initializeUI() {
        if (initialized) {
            return;
        }

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::initializeUI);
            return;
        }

        try {
            stage = new Stage();
            stage.setTitle("Menú Principal - Paciente");
            stage.setResizable(false);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventanaPacientePrincipal.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load(), 640, 400);
            stage.setScene(scene);
            stage.setOnCloseRequest(e -> handleSalir());
            initialized = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setControlador(ControlPaciente controlador) {
        this.controlador = controlador;
    }

    public void muestra() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::muestra);
            return;
        }
        oculta();
        initializeUI();
        stage.show();
    }


    /**
     * Método para ocultar la ventana sin cerrar la aplicación.
     */
    public void oculta() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::oculta);
            return;
        }
        if (stage != null) {
            stage.hide(); 
        }
    }

    
    @FXML
    private void handleSalir() {
        if (controlador != null) {
            controlador.salir();
        }
    }

    /**
     * Manejador para el botón "Registro emocional"
     */
    @FXML
    private void handleRegistroEmocional() {
        if (controlador != null) {
            controlador.iniciarRegistroEmocional();
        }
    }

    /**
     * Manejador para el botón "Lista de registros"
     */
    @FXML
    private void handleListaRegistros() {
        if (controlador != null) {
            controlador.iniciarListaRegistros();
        }
    }

    @FXML
    private void handleLineaCaptura() {
        if (controlador != null) {
            controlador.iniciarLineaCaptura();
        }
    }

    @FXML
    private void handlePerfilPaciente() {
        if (controlador != null) {
            controlador.iniciarPerfilPaciente();
        }
    }
}