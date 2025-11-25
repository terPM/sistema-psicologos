package mx.uam.ayd.proyecto.presentacion.pacientePrincipal;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class VentanaPacientePrincipal {

    private Stage stage;
    private ControlPaciente controlador;
    private boolean initialized = false;

    @FXML private TextArea avisoDisplayArea;

    private void initializeUI() {
        if (initialized) return;
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
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void setControlador(ControlPaciente controlador) { this.controlador = controlador; }

    public void muestra() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::muestra);
            return;
        }
        oculta();
        initializeUI();
        stage.show();
    }

    public void oculta() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::oculta);
            return;
        }
        if (stage != null) stage.hide();
    }

    public void setAvisos(String texto) { if (avisoDisplayArea != null) avisoDisplayArea.setText(texto); }

    public void muestraAviso(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML private void handleSalir() { controlador.salir(); }
    @FXML private void handleRegistroEmocional() { controlador.iniciarRegistroEmocional(); }
    @FXML private void handleLineaCaptura() { controlador.iniciarLineaCaptura(); }
    @FXML private void handleHistorialPagos() { controlador.iniciarHistorialPagos(); }
    @FXML private void handleCrearCita() { controlador.iniciarCrearCita(); }
    @FXML private void handleProximasCitas() { controlador.iniciarListarCitas(); }
    @FXML private void handleReagendarCita() { controlador.iniciarReagendarCita(); }
    @FXML private void handlePerfilPaciente() { controlador.iniciarPerfilPaciente(); }

    // NUEVO HANDLER
    @FXML private void handleActualizarInformacion() { controlador.iniciarActualizarInformacion(); }
}