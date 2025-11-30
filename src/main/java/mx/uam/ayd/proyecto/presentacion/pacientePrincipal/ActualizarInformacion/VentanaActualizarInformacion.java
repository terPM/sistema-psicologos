package mx.uam.ayd.proyecto.presentacion.pacientePrincipal.ActualizarInformacion;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import java.io.IOException;

/**
 * Clase que gestiona la interfaz gráfica para la actualización de datos.
 * Carga el FXML y maneja los eventos de la vista.
 */
@Component
public class VentanaActualizarInformacion {

    private Stage stage;
    private ControlActualizarInformacion controlador;
    private boolean initialized = false;

    @FXML private TextField txtUsuario;
    @FXML private TextField txtNombre;
    @FXML private TextField txtEdad;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCorreo;
    @FXML private PasswordField pfActual;
    @FXML private PasswordField pfNueva;
    @FXML private PasswordField pfConfirmar;

    /**
     * Asocia el controlador lógico a esta vista.
     * @param controlador El controlador encargado de la lógica.
     */
    public void setControlador(ControlActualizarInformacion controlador) {
        this.controlador = controlador;
    }

    /**
     * Método privado para cargar el archivo FXML y configurar el Stage.
     * Se asegura de ejecutarse en el hilo de JavaFX.
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
            stage.setTitle("Actualizar Información");
            stage.setResizable(false);
            // Asegúrate que este FXML exista en resources/fxml/
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventanaActualizarInformacion.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setOnCloseRequest(e -> handleCancelar());
            initialized = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra la ventana y precarga la información del paciente en los campos de texto.
     * @param paciente Objeto con los datos a mostrar.
     */
    public void muestra(Paciente paciente) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> this.muestra(paciente));
            return;
        }

        initializeUI();

        if (txtUsuario != null) {
            txtUsuario.setText(paciente.getUsuario());
        }
        if (txtNombre != null) {
            txtNombre.setText(paciente.getNombre());
        }
        if (txtEdad != null) {
            txtEdad.setText(String.valueOf(paciente.getEdad()));
        }
        if (txtTelefono != null) {
            txtTelefono.setText(paciente.getTelefono());
        }
        if (txtCorreo != null) {
            txtCorreo.setText(paciente.getCorreo());
        }

        if (pfActual != null) {
            pfActual.clear();
        }
        if (pfNueva != null) {
            pfNueva.clear();
        }
        if (pfConfirmar != null) {
            pfConfirmar.clear();
        }

        stage.show();
    }

    /**
     * Oculta la ventana actual si está visible.
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

    /**
     * Método invocado por el botón de actualizar en la vista.
     * Recolecta los datos y llama al controlador.
     */
    @FXML
    private void handleActualizarDatos() {
        controlador.actualizarDatos(
                txtUsuario.getText(), txtEdad.getText(), txtTelefono.getText(), txtCorreo.getText(),
                pfActual.getText(), pfNueva.getText(), pfConfirmar.getText()
        );
    }

    /**
     * Método invocado por el botón cancelar o al cerrar la ventana.
     * Limpia y cierra la vista.
     */
    @FXML
    private void handleCancelar() {
        oculta();
        if (controlador != null) {
            controlador.regresa();
        }
    }

    /**
     * Muestra una alerta emergente al usuario.
     * * @param Título de la ventana de alerta.
     * @param mensaje Contenido del mensaje.
     * @param tipo Tipo de alerta (ERROR, WARNING, INFORMATION, etc).
     */
    public void muestraMensaje(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}