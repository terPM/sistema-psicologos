package mx.uam.ayd.proyecto.presentacion.crearCita;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.*;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;

import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Component
public class VentanaCrearCita {
    @FXML 
    private TextField idPaciente;
    @FXML 
    private TextField idPsicologo;
    @FXML
    private ComboBox<Paciente> seleccionPaciente;
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

            // Configuración de la ventana al inicializar
            setupPacienteComboBox();
            
            // Hacer campos no editables, ya que se auto-rellenarán
            idPaciente.setEditable(false);
            idPsicologo.setEditable(false);
            nombrePsicologo.setEditable(false);

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
        idPsicologo.setText("");
        nombrePsicologo.setText("");
        fechaCita.setValue(null);
        seleccionHorario.getSelectionModel().clearSelection();
        seleccionHorario.getItems().clear();
        seleccionHorario.setPromptText("Seleccione una fecha primero");
        seleccionPaciente.getSelectionModel().clearSelection();
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

    @FXML
    private void onPacienteSeleccionado(ActionEvent event) {
        Paciente pacienteSeleccionado = seleccionPaciente.getValue();
        
        if (pacienteSeleccionado != null) {
            // Auto-rellenar campos
            idPaciente.setText(String.valueOf(pacienteSeleccionado.getId()));
            
            Psicologo psicologo = pacienteSeleccionado.getPsicologo();
            if (psicologo != null) {
                idPsicologo.setText(String.valueOf(psicologo.getId()));
                nombrePsicologo.setText(psicologo.getNombre());
            } else {
                idPsicologo.setText("N/A");
                nombrePsicologo.setText("Sin psicólogo asignado");
            }
        } else {
            // Limpiar si no hay selección
            idPaciente.setText("");
            idPsicologo.setText("");
            nombrePsicologo.setText("");
        }
    }

    /**
     * Carga la lista de pacientes en el ComboBox
     */
    public void setPacientes(List<Paciente> pacientes) {
        seleccionPaciente.getItems().clear();
        seleccionPaciente.getItems().addAll(pacientes);
    }

    /**
     * Configura el ComboBox de pacientes para que muestre el nombre
     * pero mantenga el objeto Paciente internamente.
     */
    private void setupPacienteComboBox() {
        seleccionPaciente.setConverter(new StringConverter<Paciente>() {
            @Override
            public String toString(Paciente paciente) {
                return (paciente == null) ? "Seleccione un paciente" : paciente.getNombre();
            }

            @Override
            public Paciente fromString(String string) {
                // No lo usamos para selección, solo para mostrar
                return null;
            }
        });
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
        Paciente paciente = seleccionPaciente.getValue();
        LocalDate fecha = fechaCita.getValue();
        String horario = seleccionHorario.getValue();
        
        // 2. El nombre del psicólogo lo tomamos del campo de texto (que se auto-rellenó)
        String psicologoNombre = nombrePsicologo.getText();
        
        // 3. Pasamos al control para guardar
        controlCrearCita.guardarCita(paciente, psicologoNombre, fecha, horario);
    }

    //@FXML
    //private void onCancelar(ActionEvent event) {
    //    cerrar();
    //}
}