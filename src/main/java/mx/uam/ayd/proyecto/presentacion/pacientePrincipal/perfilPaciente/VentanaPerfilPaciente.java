package mx.uam.ayd.proyecto.presentacion.pacientePrincipal.perfilPaciente;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import java.io.IOException;

@Component
public class VentanaPerfilPaciente {

    private Stage stage;
    private ControlPerfilPaciente controlador;
    private boolean initialized = false;

    @FXML private Label usuario;
    @FXML private Label nombre;
    @FXML private Label edad;
    @FXML private Label telefono;
    @FXML private Label correo;

    // CAMBIO: Etiqueta para el psicólogo en lugar de campos de contraseña
    @FXML private Label psicologo;

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

        if (usuario != null) usuario.setText(paciente.getUsuario());
        if (nombre != null) nombre.setText(paciente.getNombre());
        if (edad != null) edad.setText(String.valueOf(paciente.getEdad()));
        if (telefono != null) telefono.setText(paciente.getTelefono());
        if (correo != null) correo.setText(paciente.getCorreo());

        // Lógica para mostrar el psicólogo asignado
        if (psicologo != null) {
            if (paciente.getPsicologo() != null) {
                psicologo.setText(paciente.getPsicologo().getNombre());
            } else {
                psicologo.setText("Sin asignar");
            }
        }

        stage.show();
    }

    public void oculta() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::oculta);
            return;
        }
        if (stage != null) stage.hide();
    }

    @FXML
    private void handleCancelar() {
        oculta();
        if (controlador != null) controlador.regresa();
    }

    @FXML
    private void handleIrAActualizar() {
        if (controlador != null) controlador.irAActualizarInformacion();
    }
}