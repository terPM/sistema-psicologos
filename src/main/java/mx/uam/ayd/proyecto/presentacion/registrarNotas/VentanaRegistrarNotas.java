package mx.uam.ayd.proyecto.presentacion.registrarNotas;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class VentanaRegistrarNotas {

    @FXML
    private ComboBox<Paciente> comboPaciente;

    @FXML
    private TextField txtFecha;

    @FXML
    private TextArea txtNota;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnCancelar;

    private ControlRegistrarNotas control;
    private Stage stage;

    /**
     * Muestra la ventana con los datos iniciales.
     */
    public void muestra(ControlRegistrarNotas control, List<Paciente> pacientes) {
        this.control = control;

        try {
            // ⚡️ Cargar el FXML y asignar este mismo controlador
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-RegistrarNotas.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());

            stage = new Stage();
            stage.setTitle("Registrar Notas del Paciente");
            stage.setScene(scene);

            // Llenamos el ComboBox con los pacientes
            comboPaciente.getItems().setAll(pacientes);

            //  Configuramos cómo se mostrará el texto en el ComboBox sin usar toString()
            comboPaciente.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Paciente paciente, boolean empty) {
                    super.updateItem(paciente, empty);
                    if (empty || paciente == null) {
                        setText(null);
                    } else {
                        setText(paciente.getNombre() + " (ID: " + paciente.getId() + ")");
                    }
                }
            });

            comboPaciente.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Paciente paciente, boolean empty) {
                    super.updateItem(paciente, empty);
                    if (empty || paciente == null) {
                        setText(null);
                    } else {
                        setText(paciente.getNombre() + " (ID: " + paciente.getId() + ")");
                    }
                }
            });

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            muestraMensajeError("Error al cargar la ventana de registro de notas.");
        }
    }

    /**
     * Acción del botón Guardar
     */
    @FXML
    private void guardarNota() {
        if (control != null) {
            control.guardarNota(comboPaciente.getValue(), txtNota.getText(), txtFecha.getText());
        }
    }

    /**
     * Acción del botón Cancelar
     */
    @FXML
    private void cancelarAccion() {
        if (control != null) {
            control.cancelar();
        } else {
            cierra();
        }
    }

    /**
     * Muestra mensaje de éxito
     */
    public void muestraMensajeExito(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra mensaje de error
     */
    public void muestraMensajeError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Cierra la ventana
     */
    public void cierra() {
        if (stage != null) {
            stage.close();
        }
    }
}
