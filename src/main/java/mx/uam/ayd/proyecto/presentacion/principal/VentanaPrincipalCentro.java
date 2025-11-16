package mx.uam.ayd.proyecto.presentacion.principal;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import java.io.IOException;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

@Component
public class VentanaPrincipalCentro {

    @FXML private TextField textFieldUsuario;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> miComboBox;

    private Stage stage;
    private ControlPrincipalCentro control;
    private boolean initialized = false;

    // NOTA: Tu FXML no tenía el initialize, así que lo moví a 'muestra()'

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
            stage.setTitle("Login - Centro Psicológico");
            stage.setResizable(false);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventanaPrincipal.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load(), 640, 420);
            stage.setScene(scene);

            stage.setOnCloseRequest(e -> Platform.exit());

            initialized = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setControlLoginPrincipal(ControlPrincipalCentro control) {
        this.control = control;
    }

    public void muestra() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> this.muestra());
            return;
        }

        initializeUI();

        textFieldUsuario.clear();
        passwordField.clear();

        // Llenar ComboBox
        if (miComboBox != null) {
            miComboBox.getItems().clear();
            miComboBox.getItems().addAll("Psicólogo", "Administrador", "Paciente");
            miComboBox.setValue(null);
        }

        stage.show();
    }

    @FXML
    private void handleIngresar() {
        String usuario = textFieldUsuario.getText();
        String contrasena = passwordField.getText();
        String rol = miComboBox.getValue();

        if (control != null) {
            control.autenticar(rol, usuario, contrasena);
        }
    }

    public void mostrarError(String mensaje) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de autenticación");
            alert.setHeaderText(null);
            alert.setContentText(mensaje);
            alert.showAndWait();
        });
    }

    public void cerrarLogin() {
        if (stage != null) {
            stage.hide();
        }
    }
}