package mx.uam.ayd.proyecto.presentacion.pacientePrincipal;

import javafx.application.Platform;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.reagendarCita.ControlReagendarCita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Lazy;
import mx.uam.ayd.proyecto.negocio.ServicioAviso;
import mx.uam.ayd.proyecto.negocio.modelo.Aviso;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import mx.uam.ayd.proyecto.negocio.ServicioCita;
import mx.uam.ayd.proyecto.negocio.modelo.Cita;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.RegistroEmocinal.ControlRegistroEmocinal;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.lineaCaptura.ControlLineaCaptura;
import mx.uam.ayd.proyecto.presentacion.principal.ControlPrincipalCentro;
import mx.uam.ayd.proyecto.presentacion.crearCita.ControlCrearCita;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.ListarCitas.ControlListarCitas;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.HistorialPagos.ControlHistorialPagos;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.perfilPaciente.ControlPerfilPaciente;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.ActualizarInformacion.ControlActualizarInformacion;

@Component
public class ControlPaciente {

    @Autowired private VentanaPacientePrincipal ventana;
    @Autowired private ControlRegistroEmocinal controlRegistroEmocinal;
    @Autowired @Lazy private ControlLineaCaptura controlLineaCaptura;
    @Autowired private ServicioCita servicioCita;
    @Autowired private ControlHistorialPagos controlHistorialPagos;
    @Autowired private ControlCrearCita controlCrearCita;
    @Autowired private ControlListarCitas controlListarCitas;
    @Autowired private ControlReagendarCita controlReagendarCita;
    @Autowired private ServicioAviso servicioAviso;
    @Autowired private ControlPerfilPaciente controlPerfilPaciente;
    @Autowired private ControlActualizarInformacion controlActualizarInformacion;

    private ControlPrincipalCentro controlPrincipal;
    private Paciente pacienteSesion;

    public void inicia(Paciente paciente, ControlPrincipalCentro controlPrincipal) {
        this.pacienteSesion = paciente;
        this.controlPrincipal = controlPrincipal;
        ventana.setControlador(this);
        ventana.muestra();
        cargarAvisos();
    }

    private void cargarAvisos() {
        try {
            Aviso ultimoAviso = servicioAviso.obtenerUltimoAviso();
            if (ultimoAviso != null) {
                LocalDate fecha = ultimoAviso.getFecha();
                String contenido = ultimoAviso.getContenido();
                String fechaFormateada = "Publicado el: " + fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                ventana.setAvisos(fechaFormateada + "\n\n" + contenido);
            } else {
                ventana.setAvisos("No hay avisos nuevos.");
            }
        } catch (Exception e) {
            ventana.setAvisos("Error cargando avisos.");
        }
    }

    public Paciente getPacienteSesion() { return pacienteSesion; }
    public String getNombreUsuarioActivo() { return (pacienteSesion != null) ? pacienteSesion.getUsuario() : null; }

    public void salir() {
        if (controlPerfilPaciente != null) controlPerfilPaciente.ocultaVentana();
        if (controlActualizarInformacion != null) controlActualizarInformacion.regresa();

        ventana.oculta();
        this.pacienteSesion = null;
        if (controlPrincipal != null) controlPrincipal.regresaAlLogin();
        else Platform.exit();
    }

    // Método para actualizar la sesión (llamado tras guardar cambios)
    public void actualizarSesion(Paciente pacienteActualizado) {
        this.pacienteSesion = pacienteActualizado;
    }

    public void iniciarRegistroEmocional() { if(this.pacienteSesion != null) controlRegistroEmocinal.inicia(this.pacienteSesion); }
    public void iniciarLineaCaptura() {
        if (pacienteSesion == null) return;
        Cita citaPendiente = servicioCita.buscarCitaPendienteMasReciente(pacienteSesion);
        if (citaPendiente != null) controlLineaCaptura.inicia(citaPendiente);
        else ventana.muestraAviso("Sin Pagos Pendientes", "No tienes ninguna cita pendiente de pago.");
    }
    public void iniciarCrearCita() { if (pacienteSesion != null) controlCrearCita.inicia(pacienteSesion.getUsuario()); }
    public void iniciarListarCitas() { if (pacienteSesion != null) controlListarCitas.inicia(pacienteSesion.getUsuario()); }
    public void iniciarReagendarCita() { if (pacienteSesion != null) controlReagendarCita.inicia(pacienteSesion.getUsuario()); }
    public void iniciarHistorialPagos() { if (pacienteSesion != null) controlHistorialPagos.inicia(pacienteSesion); }

    // CORRECCIÓN: Se pasa el String (getUsuario) porque el controlador original así lo pide
    public void iniciarPerfilPaciente() {
        if (pacienteSesion != null) {
            controlPerfilPaciente.inicia(pacienteSesion.getUsuario(), this);
        }
    }

    // Este sí recibe el Paciente porque es el controlador nuevo que hicimos
    public void iniciarActualizarInformacion() {
        if (pacienteSesion != null) {
            controlActualizarInformacion.inicia(pacienteSesion, this);
        }
    }
}