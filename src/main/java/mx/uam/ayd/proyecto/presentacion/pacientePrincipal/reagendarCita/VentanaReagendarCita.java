package mx.uam.ayd.proyecto.presentacion.pacientePrincipal.reagendarCita;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import mx.uam.ayd.proyecto.negocio.modelo.Cita;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Component
public class VentanaReagendarCita {

    @FXML
    private ComboBox<Cita> comboCitas;

    @FXML
    private DatePicker dateNuevaFecha;

    @FXML
    private ComboBox<String> comboNuevaHora;

    private ControlReagendarCita control;

    private Stage stage;

    public void setControl(ControlReagendarCita control) {
        this.control = control;
    }

    public void muestra() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventanaReagendarCita.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            stage = new Stage();
            stage.setTitle("Reagendar Cita");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            cargarHoras();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cerrar() {
        if (stage != null) stage.close();
    }

    public void cargarCitas(List<Cita> citas) {
        comboCitas.getItems().setAll(citas);
    }

    private void cargarHoras() {
        comboNuevaHora.getItems().clear();
        for (int h = 8; h <= 17; h++)
            comboNuevaHora.getItems().add(String.format("%02d:00", h));
    }

    @FXML
    private void handleReagendar() {
        Cita citaSel = comboCitas.getValue();
        LocalDate fecha = dateNuevaFecha.getValue();
        String hora = comboNuevaHora.getValue();

        if (citaSel == null || fecha == null || hora == null) {
            mostrarError("Debe seleccionar cita, fecha y hora.");
            return;
        }

        control.reagendarCita(citaSel.getId(), fecha, hora);
    }

    @FXML
    private void handleCancelar() {
        cerrar();
    }

    public void mostrarError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    public void mostrarExito(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
