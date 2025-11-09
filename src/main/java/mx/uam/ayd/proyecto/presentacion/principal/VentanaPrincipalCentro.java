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
    @FXML private PasswordField passwordFieldOculto;
    @FXML private TextField passwordFieldVisible;
    @FXML private Button btnTogglePassword;
    @FXML private ComboBox<String> miComboBox;

    private boolean mostrando = false;
    private Stage stage;
    private ControlPrincipalCentro control; // Referencia al controlador renombrado
    private boolean initialized = false;

    @FXML
    private void initialize() {
        // Sincroniza ambos campos para que siempre tengan el mismo texto
        if (passwordFieldVisible != null && passwordFieldOculto != null) {
            passwordFieldVisible.textProperty().bindBidirectional(passwordFieldOculto.textProperty());
        }

        // Llenar el ComboBox con los roles:
        if (miComboBox != null) {
            miComboBox.getItems().clear();
            miComboBox.getItems().addAll("Psicólogo", "Administrador", "Paciente");
            miComboBox.getSelectionModel().selectFirst();
        }
    }

    /**
     * Alterna la visibilidad de la contraseña.
     */
    @FXML
    private void togglePasswordVisibility() {
        mostrando = !mostrando;
        passwordFieldVisible.setVisible(mostrando);
        passwordFieldVisible.setManaged(mostrando);
        passwordFieldOculto.setVisible(!mostrando);
        passwordFieldOculto.setManaged(!mostrando);
        if (btnTogglePassword != null) {
            btnTogglePassword.setText(mostrando ? "O" : "V");
        }
    }

    /**
     * Inicializa la interfaz gráfica de la ventana.
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
            stage.setTitle("Login - Centro Psicológico TechSolutions");
            stage.setResizable(false);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventanaLogin.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load(), 640, 420);
            stage.setScene(scene);

            // Cerrar aplicación al cerrar ventana de login
            stage.setOnCloseRequest(e -> Platform.exit());

            initialized = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Establece el controlador para esta ventana.
     */
    public void setControlLoginPrincipal(ControlPrincipalCentro control) {
        this.control = control;
    }

    /**
     * Muestra la ventana de login.
     */
    public void muestra() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> this.muestra());
            return;
        }

        initializeUI();
        stage.show();
    }

    /**
     * Maneja el evento del botón "Ingresar". (MÉTODO RESTAURADO)
     */
    @FXML
    private void handleIngresar() {
        String usuario = textFieldUsuario.getText();
        String contrasena = passwordFieldOculto.getText();
        String rol = miComboBox.getValue();

        if (control != null) {
            control.autenticar(rol, usuario, contrasena);
        }
    }

    /**
     * Muestra un mensaje de error en una ventana de diálogo. (MÉTODO RESTAURADO)
     */
    public void mostrarError(String mensaje) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de autenticación");
            alert.setHeaderText(null);
            alert.setContentText(mensaje);
            alert.showAndWait();
        });
    }

    /**
     * Cierra la ventana de login. (MÉTODO RESTAURADO)
     */
    public void cerrarLogin() {
        if (stage != null) {
            stage.hide();
        }
    }
}