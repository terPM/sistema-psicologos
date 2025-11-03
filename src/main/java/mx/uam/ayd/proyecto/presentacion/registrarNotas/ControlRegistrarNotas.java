package mx.uam.ayd.proyecto.presentacion.registrarNotas;

import mx.uam.ayd.proyecto.negocio.ServicioNota;
import mx.uam.ayd.proyecto.negocio.ServicioPaciente;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Controlador que maneja la lógica de la ventana para registrar notas de pacientes.
 */
@Component
public class ControlRegistrarNotas {

    @Autowired
    private ServicioNota servicioNota;

    @Autowired
    private ServicioPaciente servicioPaciente;

    @Autowired
    private VentanaRegistrarNotas ventana;

    /**
     * Inicia la ventana de registro de notas.
     */
    public void inicia() {
        List<Paciente> pacientes = servicioPaciente.obtenerPacientes();
        ventana.muestra(this, pacientes);
    }

    /**
     * Guarda una nueva nota asociada a un paciente.
     */
    public void guardarNota(Paciente paciente, String texto, String fechaStr) {
        try {
            if (paciente == null || texto == null || texto.trim().isEmpty() || fechaStr == null) {
                ventana.muestraMensajeError("Debe llenar todos los campos.");
                return;
            }

            LocalDate fecha = LocalDate.parse(fechaStr, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            servicioNota.guardarNota(paciente, texto, fecha);
            ventana.muestraMensajeExito("Nota guardada correctamente.");

        } catch (DateTimeParseException e) {
            ventana.muestraMensajeError("Formato de fecha inválido. Use DD/MM/AAAA.");
        } catch (Exception e) {
            ventana.muestraMensajeError("Error al guardar la nota: " + e.getMessage());
        }
    }

    /**
     * Cierra la ventana.
     */
    public void cancelar() {
        ventana.cierra();
    }
}
