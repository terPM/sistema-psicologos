package mx.uam.ayd.proyecto.presentacion.crearCita;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Component
public class VentanaCrearCita {
    @FXML 
    private TextField idPaciente;
    @FXML
    private TextField nombrePaciente;
    @FXML 
    private TextField idPsicologo;
    @FXML
    private TextField nombrePsicologo;
    @FXML
    private ComboBox<String> seleccionHorario;
    @FXML
    private DatePicker fechaCita;

    private Stage stage;
    private ControlCrearCita controlCrearCita;
    private boolean initialized = false;

    public VentanaCrearCita() {
        // Constructor vacío
    }

    private void initializeUI() {
        if (initialized) {
            return;
        }

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::initializeUI);
            return;
        }

        try {
            stage = new Stage();
            stage.setTitle("Centro Psicológico - Crear Cita");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana_CrearCita.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load(), 640, 400);
            stage.setScene(scene);
            
            // Hacer campos no editables, ya que se auto-rellenarán
            idPaciente.setEditable(false);
            idPsicologo.setEditable(false);
            nombrePsicologo.setEditable(false);
            nombrePaciente.setEditable(false);
            initialized = true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar el archivo FXML: " + e.getMessage());
        }
    }

    public void setControlCrearCita(ControlCrearCita control) {
        this.controlCrearCita = control;
    }

    public void muestra() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> this.muestra());
            return;
        }

        initializeUI();
        limpiarCampos();
        stage.show();
    }

    public void limpiarCampos() {
        idPaciente.setText("");
        nombrePaciente.setText("");
        idPsicologo.setText("");
        nombrePsicologo.setText("");
        fechaCita.setValue(null);
        seleccionHorario.getSelectionModel().clearSelection();
        seleccionHorario.getItems().clear();
        seleccionHorario.setPromptText("Seleccione una fecha primero");
    }

    public void setDatosPacienteYPsicologo(Long idPaciente, String nombrePaciente, int idPsicologo, String nombrePsicologo) {
        this.idPaciente.setText(idPaciente.toString());
        this.nombrePaciente.setText(nombrePaciente);
        this.idPsicologo.setText(Integer.toString(idPsicologo));
        this.nombrePsicologo.setText(nombrePsicologo);
    }

    public void cerrar() {
        if (stage != null) {
            stage.close();
        }
    }
    
    @FXML
    private void onFechaSeleccionada(ActionEvent event) {
        // Limpiar horarios anteriores
        seleccionHorario.getSelectionModel().clearSelection();
        seleccionHorario.getItems().clear();
        
        if(fechaCita.getValue() != null) {
            // Pedir al control los nuevos horarios
            controlCrearCita.onFechaSeleccionada(fechaCita.getValue());
        }
    }
    
    /**
     * Carga la lista de horarios disponibles en el ComboBox
     */
    public void setHorarios(List<String> horarios) {
        seleccionHorario.getItems().clear();
        if (horarios.isEmpty()) {
            seleccionHorario.setPromptText("No hay horarios disponibles");
        } else {
            seleccionHorario.setPromptText("Seleccione un horario");
            seleccionHorario.getItems().addAll(horarios);
        }
    }

    public void muestraDialogoError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void muestraDialogoExito(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void Guardar(ActionEvent event) {
        LocalDate fecha = fechaCita.getValue();
        String horario = seleccionHorario.getValue();
        controlCrearCita.guardarCita(fecha, horario);
    }

    //@FXML
    //private void onCancelar(ActionEvent event) {
    //    cerrar();
    //}
}