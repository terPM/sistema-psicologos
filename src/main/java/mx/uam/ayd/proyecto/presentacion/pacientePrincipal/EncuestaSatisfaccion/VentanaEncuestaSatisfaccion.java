package mx.uam.ayd.proyecto.presentacion.pacientePrincipal.EncuestaSatisfaccion;

import javafx.fxml.FXML; 
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Modality; 
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;

/**
 * Clase que gestiona la presentación de la interfaz de usuario de la encuesta.
 */
@Component
public class VentanaEncuestaSatisfaccion {
    
    private ControlEncuestaSatisfaccion controlEncuesta;

    private Stage stage;
    
    @FXML private ToggleGroup q1; 
    @FXML private ToggleGroup q2;
    @FXML private ToggleGroup q3;
    @FXML private ToggleGroup q4;
    @FXML private ToggleGroup q5;
    @FXML private ToggleGroup q6;
    @FXML private ToggleGroup q7;

    @FXML private TextArea resp8; 
    @FXML private TextArea resp9; 
    @FXML private Button idGuardar;
    @FXML private Button idCancelar;

    /**
     * Establece la referencia al controlador de la lógica de negocio.
     */
    @Autowired
    public void setControl(ControlEncuestaSatisfaccion control) {
        this.controlEncuesta = control;
    }
    
    /**
     * Extrae el valor numérico (1 a 4) de la respuesta seleccionada.
     */
    private int getSelectedValue(ToggleGroup group) {
        if (group == null) return 0;
        Toggle selected = group.getSelectedToggle();
        
        if (selected instanceof RadioButton) {
            RadioButton rb = (RadioButton) selected;            
            if (rb.getUserData() == null) {
                return 1;
            }
            try {
                int userDataValue = Integer.parseInt(rb.getUserData().toString());
                return userDataValue + 1; 
            } catch (NumberFormatException e) {
                System.err.println("Error: UserData de RadioButton no es un número válido. " + e.getMessage());
                return 0; 
            }
        }
        return 0; 
    }

    /**
     * Muestra la ventana de la Encuesta de Satisfacción, inicializándola una vez.
     */
    public void muestra() {
        try {
            if (stage == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventanaEncuestaDeSatisfaccion.fxml"));
                loader.setController(this); 
                Parent root = loader.load();
                stage = new Stage();
                stage.setTitle("Encuesta de Evaluación del Servicio");
                stage.setScene(new Scene(root)); 
                stage.setResizable(false);
                stage.initModality(Modality.APPLICATION_MODAL); 
            }
            limpiarCampos(); 
            stage.showAndWait(); 

        } catch (IOException e) {
            System.err.println("Error al cargar la vista FXML de la encuesta: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Limpia los campos de entrada de la encuesta antes de mostrarla.
     */
    private void limpiarCampos() {
        // Limpiar TextAreas
        if (resp8 != null) resp8.clear();
        if (resp9 != null) resp9.clear();
        
        // Deseleccionar ToggleGroups
        if (q1 != null) q1.selectToggle(null);
        if (q2 != null) q2.selectToggle(null);
        if (q3 != null) q3.selectToggle(null);
        if (q4 != null) q4.selectToggle(null);
        if (q5 != null) q5.selectToggle(null);
        if (q6 != null) q6.selectToggle(null);
        if (q7 != null) q7.selectToggle(null);
    }


    /**
     * Cierra la ventana, llamada desde el ControlEncuestaSatisfaccion.
     */
    public void cerrarVentana() {
        if (this.stage != null) {
            this.stage.close();
        }
    }

    /**
     * Maneja el evento del botón "Terminar/Guardar". Lee todos los inputs
     * y se los pasa al Control para la lógica de negocio.
     */
    @FXML
    public void handleGuardar() {
        if (controlEncuesta != null) {
            
            // 1. Recolección y conversión de respuestas de escala (Q1 a Q7)
            int q1Val = getSelectedValue(q1);
            int q2Val = getSelectedValue(q2);
            int q3Val = getSelectedValue(q3);
            int q4Val = getSelectedValue(q4);
            int q5Val = getSelectedValue(q5);
            int q6Val = getSelectedValue(q6);
            int q7Val = getSelectedValue(q7);

            // 2. Recolección de respuestas abiertas (Q8 y Q9)
            String q8 = resp8.getText();
            String q9 = resp9.getText();
            
            // 3. Delegar la validación y guardado al Control
            controlEncuesta.enviarEncuesta(q1Val, q2Val, q3Val, q4Val, q5Val, q6Val, q7Val, q8, q9);
        }
    }
    
    /**
     * Maneja el evento del botón "Cancelar".
     */
    @FXML
    public void handleCancelar() {
        if (controlEncuesta != null) {
            controlEncuesta.terminarEncuesta();
        }
    }


    /**
     * Muestra una ventana de aviso de éxito.
     */
    public void muestraAviso(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra una ventana de error.
     */
    public void muestraError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Fallo al completar la encuesta");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
