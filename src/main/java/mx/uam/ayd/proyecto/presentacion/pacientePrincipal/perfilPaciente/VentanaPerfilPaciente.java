package mx.uam.ayd.proyecto.presentacion.pacientePrincipal.perfilPaciente;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import java.io.IOException;

@Component
public class VentanaPerfilPaciente {
    
    private Stage stage;
    private ControlPerfilPaciente controlador;
    private boolean initialized = false;
    private boolean mostrando = false;


    @FXML private Label usuario;
    @FXML private Label nombre;
    @FXML private Label edad;
    @FXML private Label telefono;
    @FXML private Label correo;
    @FXML private PasswordField passwordFieldOculto;
    @FXML private TextField passwordFieldVisible;
    @FXML private Button btnTogglePassword;

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

    public void setControlador(ControlPerfilPaciente controlador) {
        this.controlador = controlador;
    }
    
    private void initializeUI() {
        if (initialized) return;

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::initializeUI);
            return;
        }

        try {
            stage = new Stage();
            stage.setTitle("Información Personal");
            stage.setResizable(false);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventanaPerfilPersonal.fxml")); 
            loader.setController(this);
            // Ajusta el tamaño de la escena si es necesario
            Scene scene = new Scene(loader.load(), 640, 400); 
            stage.setScene(scene);
            stage.setOnCloseRequest(e -> handleCancelar()); 
            
            initialized = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void muestra(Paciente paciente) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> this.muestra(paciente));
            return;
        }

        initializeUI();
        
        // Carga los datos del modelo Paciente en la vista usando los fx:id:
        if (usuario != null) usuario.setText(paciente.getUsuario());
        if (nombre != null) nombre.setText(paciente.getNombre());
        if (edad != null) edad.setText(String.valueOf(paciente.getEdad()));
        if (telefono != null) telefono.setText(paciente.getTelefono());
        if (correo != null) correo.setText(paciente.getCorreo());

        String password = paciente.getContrasena();
    
        if (passwordFieldOculto != null) {
            passwordFieldOculto.setText(password);
            passwordFieldOculto.setEditable(false);
        }
        if (passwordFieldVisible != null) {
            passwordFieldVisible.setText(password);
            passwordFieldVisible.setEditable(false);
        }
        mostrando = false;
        if (passwordFieldVisible != null) {
        passwordFieldVisible.setVisible(false);
        passwordFieldVisible.setManaged(false); 
        }
        if (passwordFieldOculto != null) {
            passwordFieldOculto.setVisible(true);
            passwordFieldOculto.setManaged(true); 
        }
        if (btnTogglePassword != null) {
        btnTogglePassword.setText("V");
        }
        stage.show();
    }
    
    public void oculta() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::oculta);
            return;
        }
        if (stage != null) {
            stage.hide();
        }
    }
    
    @FXML // Vínculo con el botón "Volver" del FXML
    private void handleCancelar() {
        oculta();
        if (controlador != null) {
            controlador.regresa();
        }
    }
}