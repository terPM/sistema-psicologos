package mx.uam.ayd.proyecto.presentacion.psicologoPrincipal;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class VentanaPsicologoPrincipal {

    private Stage stage;
    private ControlPsicologo controlador;
    private boolean initialized = false;

    @FXML
    private TextArea avisoDisplayArea;
    

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
            stage.setTitle("Menú Principal - Psicólogo");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventanaPsicologoPrincipal.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load(), 640, 400);
            stage.setScene(scene);

            stage.setOnCloseRequest(e -> handleSalir());

            initialized = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setControlador(ControlPsicologo controlador) {
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

        if (controlador != null) {
            controlador.actualizarDisplayAviso();
        }
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
    @FXML
    private void handleRegistrarNotas() {
        if (controlador != null) {
            controlador.registrarNotas();
        }
    }
    @FXML
    private void handleVerHorario() {
        if (controlador != null) {
            controlador.verHorario();
        }
    }

    public void actualizarAviso(String texto) { 
    if (avisoDisplayArea != null) {
            avisoDisplayArea.setText(texto);
        }
    }

}