package mx.uam.ayd.proyecto.presentacion.pacientePrincipal.lineaCaptura;

import java.io.IOException;
import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Clase de Vista (View) para mostrar la línea de captura generada.
 * Utiliza JavaFX y es gestionada por Spring como un componente (@Component).
 * Implementa la inicialización diferida (lazy loading) para la UI de JavaFX.
 */
@Component
public class VentanaLineaCaptura {
    private Stage stage;
    private boolean initialized = false;

    @FXML
    private Label nombrePaciente;

    @FXML
    private Label idTotal;

    @FXML
    private Label idLinea;

    @FXML
    private Label idFecha;

    @FXML
    private Button idCancelar;

    /**
     * Constructor vacío requerido por Spring y FXMLLoader.
     */
    public VentanaLineaCaptura(){
        //Constructor vacío
    }

    /**
     * Inicializa los componentes internos de JavaFX (Stage, Scene, FXMLLoader).
     * Asegura que la inicialización se ejecute en el hilo de JavaFX (Platform.runLater).
     */
    private void initializeUI(){
        if (initialized) {
            return;
        }if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::initializeUI);
            return;
        }
        
        try {
            stage = new Stage();
            stage.setTitle("Linea de Captura");
            stage.setResizable(false);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/VentanaLineaCaptura.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load(), 640, 400);
            stage.setScene(scene);            
            initialized = true;
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "No se pudo cargar la ventana: " + e.getMessage()).showAndWait();
        }
    }

    /**
     * Establece la referencia al ControlLineaCaptura.
     * Aunque no se usa internamente, es necesario para el patrón de inyección.
     */
    public void setControl(ControlLineaCaptura control) {
        // En este diseño, el campo no se guarda porque las acciones de la ventana solo son 'cerrar'.
    }

    /**
     * Muestra la ventana en pantalla. 
     * Asegura la inicialización de la UI y la ejecución en el hilo de JavaFX.
     */
    public void muestra() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::muestra);
            return;
        }if (!initialized) {
            initializeUI();
        }
        stage.show();
    }

    /**
     * Recibe los datos de la línea de captura desde el controlador y los establece en los Labels FXML.
     * Se llama desde ControlLineaCaptura antes de mostrar la ventana.
     * Asegura la inicialización previa de los elementos FXML.
     */
    public void setDatosComprobante(String nombre, double total, String linea, String fecha) {
        if (!initialized) {
            initializeUI();
        }
        nombrePaciente.setText(nombre);
        idTotal.setText(String.format("$%.2f", total));
        idLinea.setText(linea);
        idFecha.setText(fecha);
    }

    /**
     * Manejador de evento FXML para el botón de 'VOlver'.
     * Simplemente llama al método interno para cerrar la ventana.
     */
    @FXML
    private void handleCancelar() {
        cerrar();
    }

    /**
     * Cierra la ventana. 
     * Usado por el controlador después de una acción exitosa.
     */
    public void cerrar() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::cerrar);
            return;
        }
        if (stage != null) {
            stage.close();
        }
    }

    /**
     * Muestra alertas de JavaFX para comunicar errores o feedback al usuario.
     * Asegura que la alerta se muestre en el hilo de la aplicación.
     */
    public void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> this.mostrarAlerta(tipo, titulo, mensaje));
            return;
        }
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
