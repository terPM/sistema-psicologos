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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-RegistrarNotas.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            stage = new Stage();
            stage.setTitle("Registrar Notas del Paciente");
            stage.setScene(scene);
            stage.show();

            comboPaciente.getItems().setAll(pacientes);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void guardarNota() {
        control.guardarNota(comboPaciente.getValue(), txtNota.getText(), txtFecha.getText());
    }

    @FXML
    private void cancelarAccion() {
        control.cancelar();
    }

    public void muestraMensajeExito(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void muestraMensajeError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void cierra() {
        if (stage != null) {
            stage.close();
        }
    }
}
