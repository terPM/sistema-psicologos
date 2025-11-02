package mx.uam.ayd.proyecto.presentacion.pacientePrincipal.ListaRegistros;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mx.uam.ayd.proyecto.negocio.modelo.RegistroEmocional;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class VentanaListaRegistros {

    private ControlListaRegistros controlListaRegistros;
    private Stage stage;

    @FXML
    private ListView<String> listViewRegistros; // Asegúrate que el fx:id coincida

    @FXML
    private Button btnCerrar;

    public void setControl(ControlListaRegistros control) {
        this.controlListaRegistros = control;
    }

    public void muestra(List<RegistroEmocional> registros) {
        try {
            if (stage == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventanaListaRegistros.fxml"));
                loader.setController(this);
                Parent root = loader.load();

                stage = new Stage();
                stage.setTitle("Historial de Registros");
                stage.setResizable(false);//TAMAÑO ESTATICO 
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
            }

            llenaLista(registros);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar la ventana de lista de registros: " + e.getMessage());
        }
    }

    public void setVisible(boolean visible) {
        if (stage != null) {
            if (visible) stage.show();
            else stage.hide();
        }
    }

    @FXML
    public void handleCerrar(ActionEvent event) {
        if (controlListaRegistros != null) {
            controlListaRegistros.termina();
        }
    }

    public void llenaLista(List<RegistroEmocional> registros) {
        if (listViewRegistros == null) {
            System.err.println("ListView no inicializado.");
            return;
        }

        listViewRegistros.getItems().clear();
        ObservableList<String> items = FXCollections.observableArrayList();

        if (registros.isEmpty()) {
            items.add("No hay registros emocionales.");
        } else {
            for (RegistroEmocional registro : registros) {
                // Formateamos el texto a mostrar
                String texto = String.format("%s - Emoción: %s",
                        registro.getFecha().toString(),
                        registro.getEmocion());

                if (registro.getNota() != null && !registro.getNota().isEmpty()) {
                    texto += "\nNota: " + registro.getNota();
                }
                items.add(texto);
            }
        }

        listViewRegistros.setItems(items);

        // Habilitar el ajuste de texto
        listViewRegistros.setCellFactory(param -> new javafx.scene.control.ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setWrapText(true); // Habilita el ajuste de línea
                }
            }
        });
    }
}