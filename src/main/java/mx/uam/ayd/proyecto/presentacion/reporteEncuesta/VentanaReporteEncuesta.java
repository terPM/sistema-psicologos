package mx.uam.ayd.proyecto.presentacion.reporteEncuesta;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML; 
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Modality; 
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Vista de Reporte de Encuesta.
 * Es el controlador FXML (setController(this)) y llama al Control de Lógica.
 */
@Component
public class VentanaReporteEncuesta {
    
    private ControlReporteEncuesta control; 

    private Stage stage;

    @FXML private Label tituloGrafica;
    @FXML private BarChart<String, Number> barChartEncuesta;
    @FXML private ListView<String> listaComentarios; 
    
    /**
     * Establece la referencia al controlador de la lógica de negocio.
     */
    public void setControl(ControlReporteEncuesta control) {
        this.control = control;
    }

    /**
     * Muestra la ventana de Reporte de Encuesta, inicializándola una vez.
     */
    public void muestra() {
        try {
            if (stage == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventanaReporteEncuesta.fxml"));
                loader.setController(this); 
                Parent root = loader.load();
                stage = new Stage();
                stage.setTitle("Reporte de Estadísticas de Encuesta");
                stage.setScene(new Scene(root)); 
                stage.setResizable(true); // Permito redimensionar si las gráficas son grandes
                stage.initModality(Modality.APPLICATION_MODAL); 
            }
            
            if (control != null) {
                control.cargarDatosIniciales();
            }

            stage.showAndWait();

        } catch (IOException e) {
            System.err.println("Error al cargar la vista FXML del reporte: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Cierra la ventana.
     */
    public void cerrarVentana() {
        if (this.stage != null) {
            this.stage.close();
        }
    }
    
    /**
     * Maneja la acción de los botones P1 a P7 (Opción Múltiple).
     * Delega la lógica de obtención de datos al Control.
     */
    @FXML
    private void mostrarGrafica(ActionEvent event) {
        Button botonPresionado = (Button) event.getSource();
        String pregunta = botonPresionado.getText();        
        if (control != null) {
            control.mostrarGrafica(pregunta);
        }
    }
    
    /**
     * Maneja la acción de los botones P8 y P9 (Preguntas Abiertas).
     * Delega la lógica de obtención de datos al Control.
     */
    @FXML
    private void mostrarComentarios(ActionEvent event) {
        Button botonPresionado = (Button) event.getSource();
        String pregunta = botonPresionado.getText(); 
        if (control != null) {
            control.mostrarComentarios(pregunta);
        }
    }

    /**
     * Maneja la acción del botón "Volver" o "Cerrar".
     */
    @FXML
    public void handleVolver() {
        if (control != null) {
            control.terminar();
        }
    }
    
    /**
     * Actualiza la interfaz para mostrar la gráfica de barras con los conteos.
     */
    public void actualizarGrafica(String pregunta, Map<Integer, Long> conteos) {
        listaComentarios.setVisible(false);
        listaComentarios.setManaged(false);
        barChartEncuesta.setVisible(true);
        barChartEncuesta.setManaged(true);

        tituloGrafica.setText("Distribución de Respuestas para " + pregunta);

        barChartEncuesta.getData().clear();
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Frecuencia"); 

        conteos.forEach((calificacion, conteo) -> {
            series.getData().add(new XYChart.Data<>(String.valueOf(calificacion), conteo));
        });
        
        barChartEncuesta.getData().add(series);
    }
    
    /**
     * Actualiza la interfaz para mostrar la lista de comentarios.
     */
    public void actualizarListaComentarios(String pregunta, List<String> comentarios) {
        barChartEncuesta.setVisible(false);
        barChartEncuesta.setManaged(false);
        listaComentarios.setVisible(true);
        listaComentarios.setManaged(true);
        
        tituloGrafica.setText("Respuestas Abiertas para " + pregunta);

        ObservableList<String> items = FXCollections.observableArrayList(comentarios);
        listaComentarios.setItems(items);
    }
    
    public void muestraError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de Reporte");
        alert.setHeaderText("Fallo al cargar las estadísticas");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
