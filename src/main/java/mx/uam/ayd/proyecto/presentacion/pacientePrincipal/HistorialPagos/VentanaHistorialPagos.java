package mx.uam.ayd.proyecto.presentacion.pacientePrincipal.HistorialPagos;

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
import mx.uam.ayd.proyecto.negocio.modelo.Cita;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class VentanaHistorialPagos {

    private ControlHistorialPagos controlHistorialPagos;
    private Stage stage;

    @FXML
    private ListView<String> listViewPagos;

    @FXML
    private Button btnCerrar;

    public void setControl(ControlHistorialPagos control) {
        this.controlHistorialPagos = control;
    }

    public void muestra(List<Cita> citas) {
        try {
            if (stage == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventanaHistorialPagos.fxml"));
                loader.setController(this);
                Parent root = loader.load();

                stage = new Stage();
                stage.setTitle("Historial de Pagos");
                stage.setResizable(false);
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
            }

            llenaLista(citas);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar la ventana de historial de pagos: " + e.getMessage());
        }
    }

    public void setVisible(boolean visible) {
        if (stage != null) {
            if (visible) {
                stage.show();
            } else {
                stage.hide();
            }
        }
    }

    @FXML
    public void handleCerrar(ActionEvent event) {
        if (controlHistorialPagos != null) {
            controlHistorialPagos.termina();
        }
    }

    public void llenaLista(List<Cita> citas) {
        if (listViewPagos == null) {
            System.err.println("ListView no inicializado.");
            return;
        }

        listViewPagos.getItems().clear();
        ObservableList<String> items = FXCollections.observableArrayList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'a las' HH:mm");

        if (citas.isEmpty()) {
            items.add("No tienes citas ni pagos registrados.");
        } else {
            for (Cita cita : citas) {

                String fecha = (cita.getFechaCita() != null) ? cita.getFechaCita().format(formatter) : "Fecha no definida";
                String monto = String.format("$%.2f", cita.getMonto());
                String linea = cita.getLineaCaptura();
                String estado = cita.getEstadoCita().toString();

                String texto = String.format(
                        "FECHA: %s\nMONTO: %s\nLÍNEA: %s\nESTADO: %s",
                        fecha, monto, linea, estado
                );

                items.add(texto);
            }
        }

        listViewPagos.setItems(items);

        listViewPagos.setCellFactory(param -> new javafx.scene.control.ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setWrapText(true);
                }
            }
        });
    }
}