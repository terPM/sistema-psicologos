package mx.uam.ayd.proyecto.presentacion.pacientePrincipal.reagendarCita;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;

/**
 * Ventana JavaFX para reagendar cita.
 * NOTA: el FXML **NO** debe contener fx:controller (lo controlamos desde aquí con loader.setController(this))
 */
@Component
public class VentanaReagendarCita {

    @FXML private TextField txtIdCita;
    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<String> cbHora;
    @FXML private ComboBox<Psicologo> cbPsicologo;
    @FXML private Label lblStatus;

    private Stage stage;
    private ControlReagendarCita control;

    // cache para poder devolver la lista al controlador (útil para seleccionar por id)
    private List<Psicologo> psicologosCache = new ArrayList<>();

    public void setControl(ControlReagendarCita control) {
        this.control = control;
    }

    /**
     * Cargar y mostrar la ventana. Se utiliza loader.setController(this) para usar el bean de Spring.
     */
    public void muestra() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventanaReagendarCita.fxml"));
            loader.setController(this);
            Parent root = loader.load();

            stage = new Stage();
            stage.setTitle("Reagendar Cita");
            stage.setScene(new Scene(root));
            stage.setResizable(false);

            // 👇 Esto asegura que los ComboBox YA EXISTEN
            stage.setOnShown(e -> {
                control.cargarDatosIniciales();
            });

            stage.show();

        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar ventanaReagendarCita.fxml", e);
        }
    }


    /* ---------- Métodos que el Controlador puede usar para actualizar la UI ---------- */

    public void mostrarMensaje(String msg) {
        if (lblStatus != null) lblStatus.setText(msg);
    }

    public void cerrar() {
        if (stage != null) stage.close();
    }

    public void setHoras(List<String> horas) {
        if (cbHora != null) {
            cbHora.getItems().setAll(horas);
        }
    }

    public void setPsicologos(List<Psicologo> psicologos) {
        psicologosCache = (psicologos == null) ? new ArrayList<>() : new ArrayList<>(psicologos);

        if (cbPsicologo != null) {
            cbPsicologo.getItems().setAll(psicologosCache);

            // Mostrar nombre del psicólogo en la lista
            cbPsicologo.setConverter(new StringConverter<Psicologo>() {
                @Override
                public String toString(Psicologo p) {
                    return (p == null) ? "" : p.getNombre();
                }

                @Override
                public Psicologo fromString(String string) {
                    return null;
                }
            });

            // también personalizar celda para mostrar nombre
            cbPsicologo.setCellFactory(listView -> new ListCell<>() {
                @Override
                protected void updateItem(Psicologo item, boolean empty) {
                    super.updateItem(item, empty);
                    setText((empty || item == null) ? "" : item.getNombre());
                }
            });
        }
    }

    // Utilidad para que Control seleccione un psicólogo concreto
    public void selectPsicologo(Psicologo psicologo) {
        if (cbPsicologo != null && psicologo != null) {
            cbPsicologo.getSelectionModel().select(psicologo);
        }
    }

    // Si la cita trae un psicólogo que no está en la lista, lo añadimos y lo seleccionamos
    public void addAndSelectPsicologo(Psicologo psicologo) {
        if (cbPsicologo != null && psicologo != null) {
            if (!psicologosCache.contains(psicologo)) {
                psicologosCache.add(psicologo);
                cbPsicologo.getItems().add(psicologo);
            }
            cbPsicologo.getSelectionModel().select(psicologo);
        }
    }

    public List<Psicologo> getPsicologos() {
        return psicologosCache;
    }

    public void setFecha(java.time.LocalDate fecha) {
        if (dpFecha != null) dpFecha.setValue(fecha);
    }

    public void setHora(String hora) {
        if (cbHora != null) cbHora.setValue(hora);
    }

    /* ---------- Métodos enlazados desde el FXML (onAction="#...") ---------- */

    @FXML
    private void cargarCita() {
        if (control != null && txtIdCita != null) {
            control.cargarCita(txtIdCita.getText().trim());
        }
    }

    @FXML
    private void guardar() {
        if (control != null) {
            control.guardar(
                    txtIdCita.getText().trim(),
                    (dpFecha == null) ? null : dpFecha.getValue(),
                    (cbHora == null) ? null : cbHora.getValue(),
                    (cbPsicologo == null) ? null : cbPsicologo.getValue()
            );
        }
    }

    @FXML
    private void cancelar() {
        if (control != null) control.cancelar();
    }
}
