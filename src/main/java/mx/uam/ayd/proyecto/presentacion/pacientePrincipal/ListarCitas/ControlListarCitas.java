package mx.uam.ayd.proyecto.presentacion.pacientePrincipal.ListarCitas;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.negocio.ServicioCita;
import mx.uam.ayd.proyecto.negocio.ServicioPaciente;
import mx.uam.ayd.proyecto.negocio.ServicioSesion;
import mx.uam.ayd.proyecto.negocio.modelo.Cita;

@Component
public class ControlListarCitas {

    @Autowired
    private VentanaListarCitas ventana;

    @Autowired
    private ServicioCita servicioCita;

    @Autowired
    private ServicioSesion servicioSesion;

    @Autowired
    private ServicioPaciente servicioPaciente;

    private String usuarioActivo;

    public void inicia() {

        usuarioActivo = servicioSesion.getUsuarioActual();

        List<Cita> citas = servicioCita.listarCitas(usuarioActivo);

        ventana.setControlador(this);
        ventana.muestra(this, citas);
    }

    public void cancelarCita() {

        Integer id = ventana.getIdCitaSeleccionada();

        if (id == null) {
            ventana.muestraError("Por favor selecciona una cita para cancelar.");
            return;
        }

        boolean confirmar = ventana.muestraConfirmacion(
                "Cancelar cita",
                "¿Seguro que deseas cancelar esta cita?\nEsta acción no se puede deshacer."
        );

        if (!confirmar) {
            return;
        }

        servicioCita.cancelarCita(id);

        ventana.muestraExito("La cita ha sido cancelada correctamente.");

        // refrescar lista
        List<Cita> nuevas = servicioCita.listarCitas(usuarioActivo);
        ventana.mostrarCitas(nuevas);
    }
}