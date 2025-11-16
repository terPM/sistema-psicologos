package mx.uam.ayd.proyecto.presentacion.listarpacientes;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mx.uam.ayd.proyecto.negocio.modelo.BateriaClinica;
import mx.uam.ayd.proyecto.negocio.modelo.HistorialClinico;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;

/**
 * Ventana de interfaz gráfica para listar pacientes y gestionar la visualización de sus datos clínicos.
 * (Documentación...)
 * @author TechSolutions
 */
@Component
public class VentanaListarPacientes {

    // Componentes FXML
    @FXML private TableView<Paciente> tablaPacientes;
    @FXML private TableColumn<Paciente, String> columnaNombre;
    @FXML private TableColumn<Paciente, String> columnaCorreo;
    @FXML private TableColumn<Paciente, String> columnaTelefono;
    @FXML private ListView<String> listaBaterias;
    @FXML private Label puntajeObtenidoLabel;
    @FXML private TextArea comentariosTextArea;
    @FXML private Button btnAbrirDetalles;
    @FXML private Button btnGuardarComentarios;

    // Paneles de historial
    @FXML private VBox historialPlaceholder;
    @FXML private GridPane historialDetailsPane;

    // Labels de historial
    @FXML private Label lblHistorialFecha;
    @FXML private Label lblHistorialMotivo;
    @FXML private Label lblHistorialConsumo;
    @FXML private Label lblHistorialDescripcion;
    @FXML private Label lblHistorialConsentimiento;
    @FXML private Button btnAsignarPsicologo;

    // Variables de estado y control
    private Stage stage;
    private ControlListarPacientes control;
    private BateriaClinica bateriaSeleccionada;
    private List<BateriaClinica> bateriasDelPaciente;
    private Paciente pacienteSeleccionado;

    /**
     * Muestra la ventana de listado de pacientes.
     * (Documentación...)
     */
    public void muestra(ControlListarPacientes control, List<Paciente> pacientes) {
        this.control = control;

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> muestra(control, pacientes));
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-listar-pacientes.fxml"));
            loader.setController(this);

            Parent root = loader.load();

            columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            columnaCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
            columnaTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

            tablaPacientes.setItems(FXCollections.observableArrayList(pacientes));

            // Listener para la selección de una batería en la lista
            listaBaterias.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> {
                        boolean isSelected = newValue != null;
                        btnAbrirDetalles.setDisable(!isSelected);
                        btnGuardarComentarios.setDisable(!isSelected);

                        if (isSelected && bateriasDelPaciente != null) {
                            bateriasDelPaciente.stream()
                                    .filter(b -> b.getTipoDeBateria().toString().equals(newValue))
                                    .findFirst()
                                    .ifPresent(b -> control.seleccionarBateria(b));
                        }
                    }
            );

            // Listener para la selección de un paciente en la tabla
            tablaPacientes.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> {
                        this.pacienteSeleccionado = newValue; // Guarda al paciente
                        btnAsignarPsicologo.setDisable(newValue == null); // Activa/desactiva el botón

                        // Notifica al controlador sobre el paciente seleccionado
                        control.seleccionarPaciente(newValue);

                    }
            );

            stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Listado de Pacientes");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            muestraDialogoDeError("No se pudo cargar la ventana de pacientes. Contacte al administrador.");
        }
    }

    /**
     * Muestra los detalles de un historial clínico
     * (Documentación...)
     */
    public void mostrarHistorialEnPestana(HistorialClinico historial) {
        historialPlaceholder.setVisible(false);
        historialPlaceholder.setManaged(false);
        historialDetailsPane.setVisible(true);
        historialDetailsPane.setManaged(true);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        lblHistorialFecha.setText(historial.getFechaElaboracion() != null ? formatter.format(historial.getFechaElaboracion()) : "N/A");
        lblHistorialMotivo.setText(historial.getMotivo());
        lblHistorialConsumo.setText(historial.getConsumoDrogas());
        lblHistorialDescripcion.setText(historial.getDescripcionDrogas() != null && !historial.getDescripcionDrogas().isEmpty() ? historial.getDescripcionDrogas() : "N/A");
        lblHistorialConsentimiento.setText(historial.isConsentimientoAceptado() ? "Sí" : "No");
    }

    /**
     * Limpia la sección del historial clínico
     * (Documentación...)
     */
    public void limpiarHistorialEnPestana() {
        historialPlaceholder.setVisible(true);
        historialPlaceholder.setManaged(true);
        historialDetailsPane.setVisible(false);
        historialDetailsPane.setManaged(false);
    }

    /**
     * Limpia y resetea todos los campos relacionados con las baterías.
     * (Documentación...)
     */
    public void limpiarDetallesDeBateria() {
        this.bateriaSeleccionada = null;
        this.bateriasDelPaciente = null;
        listaBaterias.getItems().clear();
        puntajeObtenidoLabel.setText("-");
        comentariosTextArea.clear();
        btnAbrirDetalles.setDisable(true);
        btnGuardarComentarios.setDisable(true);
    }

    /**
     * Muestra la lista de baterías clínicas de un paciente.
     * (Documentación...)
     */
    public void mostrarBaterias(List<BateriaClinica> baterias) {
        this.bateriasDelPaciente = baterias;
        if (baterias != null) {
            List<String> nombresBaterias = baterias.stream()
                    .map(bateria -> bateria.getTipoDeBateria().toString())
                    .collect(Collectors.toList());
            listaBaterias.setItems(FXCollections.observableArrayList(nombresBaterias));
        } else {
            listaBaterias.getItems().clear();
        }
    }

    /**
     * Muestra los detalles (calificación y comentarios) de una batería.
     * (Documentación...)
     */
    public void mostrarDetallesBateria(BateriaClinica bateria) {
        this.bateriaSeleccionada = bateria;
        puntajeObtenidoLabel.setText(String.valueOf(bateria.getCalificacion()));
        comentariosTextArea.setText(bateria.getComentarios());
    }

    /**
     * Cierra la ventana actual.
     */
    public void cierra() {
        if (stage != null) {
            stage.close();
        }
    }

    /**
     * Muestra un diálogo informativo.
     * (Documentación...)
     */
    public void muestraDialogoDeInformacion(String mensaje) {
        new Alert(AlertType.INFORMATION, mensaje).showAndWait();
    }

    /**
     * Muestra un diálogo de error.
     * (Documentación...)
     */
    public void muestraDialogoDeError(String mensaje) {
        new Alert(AlertType.ERROR, mensaje).showAndWait();
    }

    /**
     * Evento manejador para el botón de guardar comentarios.
     * (Documentación...)
     */
    @FXML
    private void handleGuardarComentarios() {
        control.guardarComentarios(bateriaSeleccionada, comentariosTextArea.getText());
    }

    /**
     * Evento manejador para el botón de cerrar.
     * (Documentación...)
     */
    @FXML
    private void handleCerrar() {
        control.cerrar();
    }

    /**
     * Evento manejador para el botón de abrir detalles.
     * (Documentación...)
     */
    @FXML
    private void handleAbrirDetalles() {
        if (bateriaSeleccionada != null) {
            control.abrirDetallesBateria(bateriaSeleccionada);
        }
    }

    /**
     * Evento manejador para el botón "Asignar Psicólogo".
     * Llama al controlador para iniciar el flujo de asignación.
     */
    @FXML
    private void handleAsignarPsicologo() {
        if (pacienteSeleccionado != null) {
            control.asignarPsicologo(pacienteSeleccionado);
        }
    }
}