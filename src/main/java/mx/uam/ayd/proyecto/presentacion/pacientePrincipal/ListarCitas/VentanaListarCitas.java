package mx.uam.ayd.proyecto.presentacion.pacientePrincipal.ListarCitas;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import mx.uam.ayd.proyecto.negocio.modelo.Cita;

@Component
public class VentanaListarCitas {

    @FXML
    private TableView<Cita> tablaCitasProximas;

    @FXML
    private TableColumn<Cita, String> columnaIdCita;

    @FXML
    private TableColumn<Cita, String> columnaNombrePsicologo;

    @FXML
    private TableColumn<Cita, String> columnaFechaYHoraCita;

    private ControlListarCitas controlador;
    private Stage stage;

    private final DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void setControlador(ControlListarCitas controlador) {
        this.controlador = controlador;
    }

    public void muestra(ControlListarCitas control, List<Cita> citas) {
        this.controlador = control;

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> muestra(control, citas));
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventanaListarCitas.fxml"));
            loader.setController(this);

            Parent root = loader.load();

            mostrarCitas(citas);

            stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Próximas Citas");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            muestraError("No se pudo cargar la ventana de listado de citas.");
        }
    }

    public void cargarCitas(List<Cita> citas) {
        mostrarCitas(citas);
    }

    private void mostrarCitas(List<Cita> citas) {

        citas.sort(Comparator.comparing(Cita::getFechaCita));

        ObservableList<Cita> datos = FXCollections.observableArrayList(citas);
        tablaCitasProximas.setItems(datos);

        // Configurar columnas
        columnaIdCita.setCellValueFactory(c ->
                new SimpleStringProperty("" + c.getValue().getId()));
        columnaIdCita.setVisible(false);

        columnaNombrePsicologo.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getPsicologo().getNombre()));

        columnaFechaYHoraCita.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getFechaCita().format(formato))
        );
    }

    public Integer getIdCitaSeleccionada() {
        Cita cita = tablaCitasProximas.getSelectionModel().getSelectedItem();
        if (cita == null) {
            return null;
        }
        return cita.getId();
    }

    /**
     * Obtiene la ventana actual
    */
    public Stage getStage() {
        return (Stage) tablaCitasProximas.getScene().getWindow();
    }

    public void muestraError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void muestraExito(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Operación exitosa");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public boolean muestraConfirmacion(String titulo, String mensaje) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        ButtonType btnSi = new ButtonType("Sí");
        ButtonType btnNo = new ButtonType("No");

        alert.getButtonTypes().setAll(btnSi, btnNo);

        return alert.showAndWait().orElse(btnNo) == btnSi;
    }

    @FXML
    private void handleCancelarCita() {
        if (controlador != null) {
            controlador.cancelarCita();
        }
    }

    @FXML
    private void handleExportarPDF() {
        if (controlador != null) {
            controlador.generarPDFCita();
        }
    }
}
