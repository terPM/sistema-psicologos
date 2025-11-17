package mx.uam.ayd.proyecto.presentacion.pacientePrincipal.ListarCitas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.negocio.ServicioCita;
import mx.uam.ayd.proyecto.negocio.modelo.Cita;

import java.util.List;

@Component
public class ControlListarCitas {

    @Autowired
    private VentanaListarCitas ventana;

    @Autowired
    private ServicioCita servicioCita;

    private String nombreUsuarioActivo;

    /**
     * Inicia la ventana de listar citas del paciente
     */
    public void inicia(String nombreUsuarioActivo) {
        this.nombreUsuarioActivo = nombreUsuarioActivo;

        List<Cita> citas = servicioCita.listarCitas(nombreUsuarioActivo);

        ventana.setControlador(this);
        ventana.muestra(this, citas);
    }

    /**
     * Cancela la cita seleccionada desde la ventana
     */
    public void cancelarCita() {

        Integer idCita = ventana.getIdCitaSeleccionada();

        if (idCita == null) {
            ventana.muestraError("Debe seleccionar una cita para cancelar.");
            return;
        }

        boolean confirmado = ventana.muestraConfirmacion(
                "Confirmar cancelación",
                "¿Está seguro de cancelar la cita seleccionada?"
        );

        if (!confirmado) {
            return;
        }

        try {
            servicioCita.cancelarCita(idCita);
            ventana.muestraExito("La cita fue cancelada correctamente.");

            // Recargar tabla con citas actualizadas
            List<Cita> citas = servicioCita.listarCitas(nombreUsuarioActivo);
            ventana.cargarCitas(citas);

        } catch (Exception e) {
            ventana.muestraError("Error al cancelar cita: " + e.getMessage());
        }
    }
}
