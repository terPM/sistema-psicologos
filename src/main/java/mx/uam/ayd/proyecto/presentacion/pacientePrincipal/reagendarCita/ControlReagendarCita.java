package mx.uam.ayd.proyecto.presentacion.pacientePrincipal.reagendarCita;

import mx.uam.ayd.proyecto.negocio.ServicioCita;
import mx.uam.ayd.proyecto.negocio.modelo.Cita;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import mx.uam.ayd.proyecto.negocio.ServicioPaciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ControlReagendarCita {

    @Autowired
    private VentanaReagendarCita ventana;

    @Autowired
    private ServicioCita servicioCita;

    @Autowired
    private ServicioPaciente servicioPaciente;

    private String nombreUsuarioActivo;

    /**
     * Inicia la ventana de reagendar cita y carga las citas activas del paciente.
     */
    public void inicia(String nombreUsuarioActivo) {
        this.nombreUsuarioActivo = nombreUsuarioActivo;

        // Verificar que el paciente existe
        Paciente paciente = servicioPaciente.obtenerPacientePorUsuario(nombreUsuarioActivo);
        if (paciente == null) {
            System.err.println("Error: No se encontró el paciente con usuario: " + nombreUsuarioActivo);
            return;
        }

        // Trae todas las citas NO canceladas
        List<Cita> citas = servicioCita.listarCitas(nombreUsuarioActivo);

        ventana.setControl(this);
        ventana.muestra();
        ventana.cargarCitas(citas);
    }

    /**
     * Lógica para reagendar la cita.
     */
    public void reagendarCita(int idCita, LocalDate nuevaFecha, String nuevaHora) {
        try {
            Paciente paciente = servicioPaciente.obtenerPacientePorUsuario(nombreUsuarioActivo);
            if (paciente == null) {
                ventana.mostrarError("Error: No se encontró el paciente.");
                return;
            }

            // Reagendar en servicio
            servicioCita.reagendarCita(idCita, nuevaFecha, nuevaHora, paciente.getPsicologo());

            ventana.mostrarExito("La cita fue reagendada correctamente.");
            ventana.cerrar();

        } catch (Exception e) {
            ventana.mostrarError("Error al reagendar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
