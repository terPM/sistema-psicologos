package mx.uam.ayd.proyecto.presentacion.publicarAviso;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * Ventana para publicar un nuevo aviso.
 * * Esta clase representa la capa de presentación (vista) que se encarga de:
 * <ul>
 * <li>Mostrar un formulario para que el administrador escriba un aviso.</li>
 * <li>Capturar la acción de "Publicar" o "Cancelar".</li>
 * </ul>
 * * Es controlada por {@link ControlPublicarAviso}, quien se encarga de
 * gestionar la lógica de negocio.
 * * El archivo FXML asociado es <code>/fxml/ventanaPublicarAviso.fxml</code>.
 * * @author TechSolutions (adaptado de VentanaListarPsicologo)
 */
@Component
public class VentanaPublicarAviso {

    private ControlPublicarAviso controlPublicarAviso;
    private Stage stage;

    // Definimos los componentes FXML que tendrá la ventana
    // Debes asegurarte de que tu FXML tenga estos fx:id
    @FXML
    private TextArea textAreaAviso;

    @FXML
    private Button btnPublicar;

    @FXML
    private Button btnCancelar;

    /**
     * Establece la referencia al controlador de la vista.
     * * @param control instancia de {@link ControlPublicarAviso} que gestiona la lógica de negocio
     */
    public void setControl(ControlPublicarAviso control) {
        this.controlPublicarAviso = control;
    }

    /**
     * Muestra la ventana para publicar un aviso.
     * <p>
     * Si la ventana no ha sido creada aún, carga el archivo FXML, configura la escena 
     * y crea el escenario.
     * </p>
     */
    public void muestra() {
        try {
            if (stage == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventanaPublicarAviso.fxml"));
                loader.setController(this);
                Parent root = loader.load();

                stage = new Stage();
                stage.setTitle("Publicar Nuevo Aviso");
                stage.setScene(new Scene(root));
                
                // Hacemos la ventana MODAL para que el usuario deba cerrarla
                // antes de poder interactuar con la ventana principal
                stage.initModality(Modality.APPLICATION_MODAL);
            }
            
            // Limpiamos el texto anterior antes de mostrar
            if (textAreaAviso != null) {
                textAreaAviso.clear();
            }
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar la ventana de publicar aviso: " + e.getMessage());
        }
    }

    /**
     * Cambia la visibilidad de la ventana.
     *
     * @param visible {@code true} para mostrar la ventana, {@code false} para ocultarla
     */
    public void setVisible(boolean visible) {
        if (stage != null) {
            if (visible) {
                stage.show();
            } else {
                stage.hide();
            }
        }
    }

    /**
     * Maneja el evento del botón "Publicar".
     * <p>
     * Obtiene el texto del TextArea y se lo pasa al controlador para que lo procese.
     * </p>
     */
    @FXML
    public void handlePublicar(ActionEvent event) {
        if (controlPublicarAviso != null) {
            String contenido = textAreaAviso.getText();
            controlPublicarAviso.publicarAviso(contenido);
        }
    }

    /**
     * Maneja el evento del botón "Cancelar".
     * <p>
     * Llama al método {@link ControlPublicarAviso#termina()} para cerrar la ventana.
     * </p>
     */
    @FXML
    public void handleCancelar(ActionEvent event) {
        if (controlPublicarAviso != null) {
            controlPublicarAviso.termina();
        }
    }
    
    /**
     * Muestra un diálogo de error al usuario.
     * * @param mensaje El mensaje de error a mostrar.
     */
    public void muestraError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}