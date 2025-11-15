package mx.uam.ayd.proyecto.presentacion.mostrarCitasPsic; // <-- CAMBIO AQUÍ

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import mx.uam.ayd.proyecto.negocio.modelo.Cita;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Ventana que muestra el horario semanal usando un GridPane.
 */
@Component
public class VentanaMostrarCitas {

    private Stage stage;
    private ControlMostrarCitasPsic controlador;
    private boolean initialized = false;

    @FXML
    private GridPane gridHorario; 

    @FXML private Label labelLunes;
    @FXML private Label labelMartes;
    @FXML private Label labelMiercoles;
    @FXML private Label labelJueves;
    @FXML private Label labelViernes;
    @FXML private Label labelSabado;
    @FXML private Label labelDomingo;

    /**
     * Inicializa la interfaz de usuario
     */
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
            stage.setTitle("Horario de Citas Semanal");

            // Carga el FXML del horario. 
            // NOTA: Asegúrate que el FXML esté en la ruta correcta en 'resources'
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventanaHorario.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);

            stage.setOnCloseRequest(e -> {
                e.consume(); 
                controlador.volver(); 
            });

            initialized = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setControlador(ControlMostrarCitasPsic controlador) {
        this.controlador = controlador;
    }

    /**
     * Muestra la ventana y pobla el horario con las citas.
     *
     * @param citas La lista de citas a mostrar.
     */
    public void muestra(List<Cita> citas) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> muestra(citas));
            return;
        }
        
        initializeUI(); 
        poblarHorario(citas); 
        stage.show();
    }

    /**
     * Recorre la lista de citas y las añade al GridPane.
     */
    private void poblarHorario(List<Cita> citas) {
        limpiarGrid();

        actualizarFechasDeSemana();
        
        for (Cita cita : citas) {
            LocalDateTime fechaCita = cita.getFechaCita();
            DayOfWeek dia = fechaCita.getDayOfWeek(); 
            int hora = fechaCita.getHour(); 

            // Col 1=Lunes, ..., 6=Sábado
            int colIndex = dia.getValue(); 
            
            // Fila 1=9am, ..., 8=4pm (16:00)
            int rowIndex = hora - 7;

            // Validar que la cita está dentro del rango visible (L-S, 9am-4pm)
            if (colIndex >= 1 && colIndex <= 7 && rowIndex >= 1 && rowIndex <= 10) {
                Label citaLabel = new Label();
                if (cita.getPaciente() != null) {
                    citaLabel.setText(cita.getPaciente().getNombre());
                } else {
                    citaLabel.setText("Cita Reservada");
                }

                citaLabel.setStyle("-fx-background-color: #abbac4ff; -fx-border-color: #000000ff; -fx-padding: 5; -fx-alignment: CENTER;");
                citaLabel.setMaxWidth(Double.MAX_VALUE);

                citaLabel.setOnMouseClicked(event -> {
                    controlador.citaSeleccionada(cita);
                });
                
                gridHorario.add(citaLabel, colIndex, rowIndex);
            }
        }
    }

    /**
     * Calcula la semana actual (Lunes a Domingo) y actualiza
     * las etiquetas del encabezado del GridPane.
     */
    private void actualizarFechasDeSemana() {
        LocalDate hoy = LocalDate.now();
        DayOfWeek diaActual = hoy.getDayOfWeek();
        int valorDiaActual = diaActual.getValue();

        LocalDate lunes = hoy.minusDays(valorDiaActual - 1); 

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

        Label[] labels = {labelLunes, labelMartes, labelMiercoles, labelJueves, labelViernes, labelSabado, labelDomingo};
        String[] nombres = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};

        for (int i = 0; i < labels.length; i++) {
            LocalDate fechaDelDia = lunes.plusDays(i);
            labels[i].setText(nombres[i] + "\n" + fechaDelDia.format(formatter));
            labels[i].setTextAlignment(TextAlignment.CENTER); 
        }
    }

    /**
     * Limpia el grid de todas las citas anteriores.
     */
    private void limpiarGrid() {
        gridHorario.getChildren().removeIf(node -> 
            (GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0) &&
            (GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) > 0) &&
            (node instanceof Label) 
        );
    }

    /**
     * Oculta la ventana.
     */
    public void oculta() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::oculta);
            return;
        }
        if (stage != null) {
            stage.hide();
        }
    }


    @FXML
    private void handleVolver() {
        if (controlador != null) {
            controlador.volver();
        }
    }
}