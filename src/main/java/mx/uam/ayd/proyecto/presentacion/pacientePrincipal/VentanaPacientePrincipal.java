package mx.uam.ayd.proyecto.presentacion.pacientePrincipal;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
<<<<<<< HEAD
import javafx.scene.control.TextArea;
=======
import javafx.scene.control.Alert;
>>>>>>> hu-16-historial-de-pagos
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class VentanaPacientePrincipal {

    private Stage stage;
    private ControlPaciente controlador;
    private boolean initialized = false;

<<<<<<< HEAD
    @FXML
    private TextArea avisoDisplayArea;
=======
    private void initializeUI() {
        if (initialized) {
            return;
        }
>>>>>>> hu-16-historial-de-pagos

    private void initializeUI() {
        if (initialized) return;

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
        oculta();
        initializeUI();
        stage.show();
    }

<<<<<<< HEAD
    public void oculta() {
        if (stage != null) stage.hide();
    }

    public void setAvisos(String texto) {
        if (avisoDisplayArea != null) {
            avisoDisplayArea.setText(texto);
        }
    }

=======

    public void oculta() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::oculta);
            return;
        }
        if (stage != null) {
            stage.hide();
        }
    }


>>>>>>> hu-16-historial-de-pagos
    @FXML
    private void handleSalir() {
        controlador.salir();
    }

    @FXML
    private void handleRegistroEmocional() {
        controlador.iniciarRegistroEmocional();
    }

<<<<<<< HEAD
    @FXML
    private void handleListaRegistros() {
        controlador.iniciarListaRegistros();
    }

=======
>>>>>>> hu-16-historial-de-pagos
    @FXML
    private void handleLineaCaptura() {
        controlador.iniciarLineaCaptura();
    }

<<<<<<< HEAD
    @FXML
    private void handleCrearCita() {
        controlador.iniciarCrearCita();
    }

    @FXML
    private void handleProximasCitas() {
        controlador.iniciarListarCitas();
    }

    @FXML
    private void handleReagendarCita() {
        controlador.iniciarReagendarCita();
    }
}
=======
    public void muestraAviso(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void handleHistorialPagos() {
        if (controlador != null) {
            controlador.iniciarHistorialPagos();
        }
    }
}
>>>>>>> hu-16-historial-de-pagos
