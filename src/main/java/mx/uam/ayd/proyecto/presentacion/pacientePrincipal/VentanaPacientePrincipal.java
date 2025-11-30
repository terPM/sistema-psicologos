package mx.uam.ayd.proyecto.presentacion.pacientePrincipal;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import java.io.IOException;
import javafx.scene.control.Button;
import mx.uam.ayd.proyecto.negocio.modelo.Notificacion;

@Component
public class VentanaPacientePrincipal {

    private Stage stage;
    private ControlPaciente controlador;
    private boolean initialized = false;

    @FXML
    private TextArea avisoDisplayArea;
    @FXML
    private Button btnEncuestaSatisfaccion;

    @FXML
    private Circle notificacionBadge;

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

    public void oculta() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::oculta);
            return;
        }
        if (stage != null) {
            stage.hide();
        }
    }

    public void setAvisos(String texto) {
        if (avisoDisplayArea != null) {
            avisoDisplayArea.setText(texto);
        }
    }

    public void muestraAviso(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void setNotificacionActiva(boolean activa) {
        if (notificacionBadge != null) {
            notificacionBadge.setVisible(activa);
        }
    }

    @FXML
    private void handleSalir() {
        controlador.salir();
    }

    @FXML
    private void handleRegistroEmocional() {
        controlador.iniciarRegistroEmocional();
    }

    @FXML
    private void handleLineaCaptura() {
        controlador.iniciarLineaCaptura();
    }

    @FXML
    private void handleHistorialPagos() {
        if (controlador != null) {
            controlador.iniciarHistorialPagos();
        }
    }

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

    @FXML
    private void handlePerfilPaciente() {
        if (controlador != null) {
            controlador.iniciarPerfilPaciente();
        }
    }

    @FXML
    private void handleAbrirEncuesta() {
        if (controlador != null) {
            controlador.handleAbrirEncuesta();
        }
    }

    @FXML
    public void setEncuestaHabilitada(boolean habilitada) {
        if (!Platform.isFxApplicationThread()) {
                Platform.runLater(() -> setEncuestaHabilitada(habilitada));
                return;
        } if (btnEncuestaSatisfaccion != null) {
                btnEncuestaSatisfaccion.setDisable(!habilitada);
        }
    }
   /**
     * HU-03: Cuando da clic en el ícono de campanita 
     * Reemplazamos el Alert simple por la llamada al controlador.
     */
    @FXML
    private void handleVerNotificaciones() {
        controlador.iniciarVerNotificaciones();
    }

    /**
     * Muestra las notificaciones listadas 
     */
    public void mostrarPanelNotificaciones(List<Notificacion> notificaciones) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> mostrarPanelNotificaciones(notificaciones));
            return;
        }
      
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Notificaciones");
        dialog.setHeaderText("Tus avisos y recordatorios");
        
        // Crear la lista visual
        ListView<Notificacion> listView = new ListView<>();
        listView.getItems().addAll(notificaciones);
        
        // Personalizar cómo se ve cada celda (Fila)
        listView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Notificacion> call(ListView<Notificacion> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Notificacion item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            // Diseño de cada fila: Fecha en negrita, mensaje abajo
                            VBox vbox = new VBox(3); // Espacio de 3px entre elementos
                            
                            // Formato de fecha
                            String fechaStr = item.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                            Label lblFecha = new Label(fechaStr);
                            lblFecha.setFont(Font.font("System", FontWeight.BOLD, 12));
                            
                            Label lblMensaje = new Label(item.getMensaje());
                            lblMensaje.setWrapText(true); // Permitir que el texto baje de línea
                            lblMensaje.setMaxWidth(350);  // Ancho máximo para el texto
                            
                            vbox.getChildren().addAll(lblFecha, lblMensaje);
                            setGraphic(vbox);
                        }
                    }
                };
            }
        });

        // Contenedor principal
        VBox container = new VBox(listView);
        container.setPrefSize(400, 300); // Tamaño del panel

        dialog.getDialogPane().setContent(container);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.initOwner(stage); // Vincular a la ventana principal
        dialog.showAndWait(); 
    }
    
}
