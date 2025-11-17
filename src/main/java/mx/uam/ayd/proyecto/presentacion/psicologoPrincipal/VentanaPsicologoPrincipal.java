package mx.uam.ayd.proyecto.presentacion.psicologoPrincipal;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import mx.uam.ayd.proyecto.negocio.modelo.Notificacion;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class VentanaPsicologoPrincipal {

    private Stage stage;
    private ControlPsicologo controlador;
    private boolean initialized = false;

<<<<<<< HEAD
    @FXML
    private TextArea avisoDisplayArea;
    

    /**
     * Inicializa la interfaz de usuario
     */
    /* ids alineados con el FXML */
    @FXML private Circle burbujaNotificacion;          // <- OJO: Circle (no Label)
    @FXML private ListView<String> listaNotificaciones;
    @FXML private Button btnCampana;

=======
>>>>>>> hu-16-historial-de-pagos
    private void initializeUI() {
        if (initialized) return;

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

    public void oculta() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::oculta);
            return;
        }
<<<<<<< HEAD
        if (stage != null) stage.hide();
=======
        if (stage != null) {
            stage.hide();
        }
>>>>>>> hu-16-historial-de-pagos
    }

    @FXML
    private void handleSalir() {
        if (controlador != null) controlador.salir();
    }

    @FXML
    private void handleRegistrarNotas() {
        if (controlador != null) controlador.registrarNotas();
    }
    @FXML
    private void handleVerHorario() {
        if (controlador != null) {
            controlador.verHorario();
        }
    }

<<<<<<< HEAD
    public void actualizarAviso(String texto) { 
    if (avisoDisplayArea != null) {
            avisoDisplayArea.setText(texto);
        }
    }

    /* ====== Notificaciones ====== */
    public void setBurbujaVisible(boolean visible) {
        if (burbujaNotificacion != null) burbujaNotificacion.setVisible(visible);
    }

    @FXML
    private void abrirNotificaciones() {
        if (controlador != null) controlador.mostrarNotificaciones();
        setBurbujaVisible(false);
    }

    public void mostrarListViewNotificaciones(List<Notificacion> notificaciones) {
        listaNotificaciones.getItems().clear();

        if (notificaciones == null || notificaciones.isEmpty()) {
            listaNotificaciones.getItems().add("No hay notificaciones nuevas");
            return;
        }

        for (Notificacion n : notificaciones) {
            String linea = String.format("[%s] %s",
                    n.getFecha().toString().replace("T", " "),
                    n.getMensaje());
            listaNotificaciones.getItems().add(linea);
        }
    }
}
=======
    @FXML
    private void handleListaRegistros() {
        if (controlador != null) {
            controlador.iniciarListaRegistros();
        }
    }
}
>>>>>>> hu-16-historial-de-pagos
