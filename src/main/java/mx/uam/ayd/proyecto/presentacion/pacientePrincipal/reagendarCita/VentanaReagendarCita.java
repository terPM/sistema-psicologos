package mx.uam.ayd.proyecto.presentacion.pacientePrincipal.reagendarCita;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ListCell;
import javafx.stage.Stage;

import mx.uam.ayd.proyecto.negocio.modelo.Cita;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Component
public class VentanaReagendarCita {

    @FXML private ComboBox<Cita> comboCitas;
    @FXML private DatePicker dateNuevaFecha;
    @FXML private ComboBox<String> comboNuevaHora;

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

            configurarDatePicker();   //
            configurarComboBox();
            cargarHoras();
            configurarListeners();

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  BLOQUEAR FECHAS PASADAS EN EL DATEPICKER
     */
    private void configurarDatePicker() {
        dateNuevaFecha.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (empty || date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #dddddd;");
                }
            }
        });

        // Por si el usuario escribe manualmente una fecha inválida
        dateNuevaFecha.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.isBefore(LocalDate.now())) {
                mostrarError("No puedes seleccionar una fecha pasada.");
                dateNuevaFecha.setValue(null);
            }
        });
    }

    /**
     * Fija cómo se muestra cada Cita en el ComboBox para evitar usar toString()
     * y prevenir LazyInitializationException.
     */
    private void configurarComboBox() {
        comboCitas.setCellFactory(list -> new ListCell<Cita>() {
            @Override
            protected void updateItem(Cita item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("Cita: " + item.getFechaCita());
                }
            }
        });

        comboCitas.setButtonCell(new ListCell<Cita>() {
            @Override
            protected void updateItem(Cita item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("Cita: " + item.getFechaCita());
                }
            }
        });
    }

    private void configurarListeners() {
        comboCitas.setOnAction(e -> {
            dateNuevaFecha.setDisable(false);
            comboNuevaHora.setDisable(false);
        });
    }

    public void cargarCitas(List<Cita> citas) {
        comboCitas.getItems().setAll(citas);
    }

    public void cerrar() {
        if (stage != null) {
            stage.close();
        }
    }

    private void cargarHoras() {
        comboNuevaHora.getItems().clear();
        for (int h = 8; h <= 17; h++) {
            comboNuevaHora.getItems().add(String.format("%02d:00", h));
        }
    }

    @FXML
    private void handleReagendar() {
        Cita citaSel = comboCitas.getValue();
        LocalDate fecha = dateNuevaFecha.getValue();
        String hora = comboNuevaHora.getValue();

        if (citaSel == null || fecha == null || hora == null) {
            mostrarError("Seleccione una cita, una fecha y un horario.");
            return;
        }

        //  VALIDACIÓN FINAL
        if (fecha.isBefore(LocalDate.now())) {
            mostrarError("No puedes reagendar una cita a una fecha pasada.");
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
