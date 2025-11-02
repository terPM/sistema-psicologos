package mx.uam.ayd.proyecto.presentacion.pacientePrincipal.RegistroEmocinal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class VentanaRegistroEmocional {

    private ControlRegistroEmocinal controlRegistroEmocional;
    private Stage stage;
    private String emocionSeleccionada;

    @FXML
    private Label lblFechaActual;

    @FXML
    private TextArea textAreaNota;

    // Botones de emoción
    @FXML
    private Button btnfeliz;
    @FXML
    private Button btntriste;
    @FXML
    private Button btnenojado;
    @FXML
    private Button btnansioso;
    @FXML
    private Button btnneutral;


    public void setControl(ControlRegistroEmocinal control) {
        this.controlRegistroEmocional = control;
    }

    public void muestra() {
        try {
            if (stage == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventanaRegistroEmocional.fxml"));
                loader.setController(this);
                Parent root = loader.load();

                stage = new Stage();
                stage.setTitle("Registro Emocional");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
            }

            textAreaNota.clear();
            emocionSeleccionada = null;
            resetearEstiloBotones();

            LocalDate fechaHoy = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy");
            lblFechaActual.setText("Fecha: " + fechaHoy.format(formatter));

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            muestraError("Error al cargar la ventana: " + e.getMessage());
        }
    }

    public void setVisible(boolean visible) {
        if (stage != null) {
            if (visible) stage.show();
            else stage.hide();
        }
    }

    /**
     * Manejador para TODOS los botones de emoción.
     */
    @FXML
    public void handleEmocionButton(ActionEvent event) {
        Button botonPresionado = (Button) event.getSource();
        emocionSeleccionada = botonPresionado.getText();


        resetearEstiloBotones();


        botonPresionado.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
    }

    private void resetearEstiloBotones() {
        btnfeliz.setStyle(null);
        btntriste.setStyle(null);
        btnenojado.setStyle(null);
        btnansioso.setStyle(null);
        btnneutral.setStyle(null);
    }

    /**
     * Manejador para el botón "Guardar".
     */
    @FXML
    public void handleGuardar(ActionEvent event) {
        String nota = textAreaNota.getText();
        controlRegistroEmocional.guardarRegistro(emocionSeleccionada, nota);
    }

    public void muestraError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void muestraAviso(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}