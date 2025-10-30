package mx.uam.ayd.proyecto.presentacion.listaAvisos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mx.uam.ayd.proyecto.negocio.modelo.Aviso;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Ventana que muestra un historial de todos los avisos publicados.
 *
 * <p>Esta clase representa la capa de presentación (vista) que se encarga de:</p>
 * <ul>
 * <li>Mostrar una lista de avisos (fecha y contenido).</li>
 * <li>Capturar la acción de "Cerrar".</li>
 * </ul>
 *
 * <p>Es controlada por {@link ControlListaAvisos}, quien se encarga de
 * obtener los datos y gestionar la lógica.</p>
 *
 * <p>El archivo FXML asociado es <code>/fxml/ventanaListaAvisos.fxml</code>.</p>
 *
 * @author TechSolutions (adaptado de VentanaPublicarAviso)
 */
@Component
public class VentanaListaAvisos {

    private ControlListaAvisos controlListaAvisos;
    private Stage stage;

    // Componentes FXML
    @FXML
    private ListView<String> listViewAvisos;

    @FXML
    private Button btnCerrar;

    /**
     * Establece la referencia al controlador de la vista.
     *
     * @param control instancia de {@link ControlListaAvisos} que gestiona la lógica
     */
    public void setControl(ControlListaAvisos control) {
        this.controlListaAvisos = control;
    }

    /**
     * Muestra la ventana con el historial de avisos.
     * <p>
     * Si la ventana no ha sido creada aún, carga el archivo FXML, configura la escena
     * y crea el escenario.
     * </p>
     */
    public void muestra(List<Aviso> avisos) {
        try {
            if (stage == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventanaListaAvisos.fxml"));
                loader.setController(this);
                Parent root = loader.load(); // <-- AQUÍ se inicializa listViewAvisos

                stage = new Stage();
                stage.setTitle("Historial de Avisos");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
            }

            llenaLista(avisos);
            
            
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar la ventana de lista de avisos: " + e.getMessage());
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
     * Maneja el evento del botón "Cerrar".
     * <p>
     * Llama al método {@link ControlListaAvisos#termina()} para cerrar la ventana.
     * </p>
     */
    @FXML
    public void handleCerrar(ActionEvent event) {
        if (controlListaAvisos != null) {
            controlListaAvisos.termina();
        }
    }

    /**
     * Recibe la lista de avisos desde el controlador y la formatea
     * para mostrarla en el ListView.
     *
     * @param avisos La lista de entidades {@link Aviso} a mostrar.
     */
    public void llenaLista(List<Aviso> avisos) {
        if (listViewAvisos == null) {
            System.err.println("ListView no inicializado. Esto no debería pasar si se llama desde muestra()");
            return;
        }

        listViewAvisos.getItems().clear();

        ObservableList<String> items = FXCollections.observableArrayList();

        if (avisos.isEmpty()) {
            items.add("No hay avisos registrados.");
        } else {
            for (Aviso aviso : avisos) {
                String textoFormateado = String.format("%s: %s",
                        aviso.getFecha().toString(),
                        aviso.getContenido());
                items.add(textoFormateado);
            }
        }
        
        listViewAvisos.setItems(items);
        
        // Opcional: Habilitar el ajuste de texto en las celdas del ListView
        // Esto es un poco más avanzado, pero útil si los avisos son largos.
        listViewAvisos.setCellFactory(param -> new javafx.scene.control.ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    setWrapText(true); // Habilita el ajuste de línea
                }
            }
        });
    }

    /**
     * Muestra un diálogo de error al usuario.
     *
     * @param mensaje El mensaje de error a mostrar.
     */
    public void muestraError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}