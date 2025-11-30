package mx.uam.ayd.proyecto.presentacion.pacientePrincipal.lineaCaptura;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import mx.uam.ayd.proyecto.negocio.ServicioLineaCaptura;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class VentanaLineaCaptura {

    private Stage stage;
    private boolean initialized = false;

    private String nombre;
    private double total;
    private String linea;
    private String fecha;

    @Autowired
    private ServicioLineaCaptura servicioLineaCaptura;

    @FXML private Label nombrePaciente;
    @FXML private Label idTotal;
    @FXML private Label idLinea;
    @FXML private Label idFecha;
    @FXML private Button btnGuardar;

    public VentanaLineaCaptura(){}

    private void initializeUI(){
        if (initialized) return;

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::initializeUI);
            return;
        }

        try {
            stage = new Stage();
            stage.setTitle("Línea de Captura");
            stage.setResizable(false);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/VentanaLineaCaptura.fxml"));
            loader.setController(this);

            Scene scene = new Scene(loader.load(), 600, 500);
            stage.setScene(scene);

            initialized = true;

            btnGuardar.setOnAction(e -> guardarPDF());

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void muestra() {
        if (!initialized) initializeUI();
        stage.show();
    }

    public void cerrar() {
        if (stage != null) stage.close();
    }

    public void setDatosComprobante(String nombre, double total, String linea, String fecha) {
        if (!initialized) initializeUI();

        this.nombre = nombre;
        this.total = total;
        this.linea = linea;
        this.fecha = fecha;

        nombrePaciente.setText(nombre);
        idTotal.setText("$" + total);
        idLinea.setText(linea);
        idFecha.setText(fecha);
    }

    private void guardarPDF() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar comprobante");

            fileChooser.setInitialFileName("comprobante_" + linea + ".pdf");

            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter("Archivo PDF (*.pdf)", "*.pdf");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {

                File pdf = servicioLineaCaptura.generarPDFPersonalizado(
                        file.getAbsolutePath(),
                        nombre,
                        total,
                        linea,
                        fecha
                );

                servicioLineaCaptura.abrirPDF(pdf);

                mostrarAlerta(Alert.AlertType.INFORMATION,
                        "Archivo Guardado",
                        "El comprobante se guardó correctamente:\n" + file.getAbsolutePath()
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR,
                    "Error al guardar PDF",
                    "Ocurrió un error al generar el archivo.");
        }
    }

    @FXML
    private void handleCancelar() {
        cerrar();
    }

    public void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
