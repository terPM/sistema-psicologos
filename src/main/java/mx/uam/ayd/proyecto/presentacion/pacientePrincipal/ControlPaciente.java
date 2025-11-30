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
import java.util.List;

import mx.uam.ayd.proyecto.negocio.ServicioCita;
import mx.uam.ayd.proyecto.negocio.ServicioNotificacion;
import mx.uam.ayd.proyecto.negocio.modelo.Cita;
import mx.uam.ayd.proyecto.negocio.modelo.Notificacion;
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

    // --- Campos de Ambas Ramas ---
    @Autowired
    private VentanaPacientePrincipal ventana;
    @Autowired
    private ControlRegistroEmocinal controlRegistroEmocinal;
    @Autowired
    @Lazy
    private ControlLineaCaptura controlLineaCaptura;
    @Autowired
    private ServicioCita servicioCita;
    @Autowired
    private ControlHistorialPagos controlHistorialPagos; // De hu-16
    @Autowired
    private ControlCrearCita controlCrearCita; // De HEAD
    @Autowired
    private ControlListarCitas controlListarCitas; // De HEAD
    @Autowired
    private ControlReagendarCita controlReagendarCita; // De HEAD
    @Autowired
    private ServicioAviso servicioAviso; // De HEAD
    @Autowired
    private ControlPerfilPaciente controlPerfilPaciente; // de hu-13
    @Autowired
    private ServicioNotificacion servicioNotificacion; //Hu-03
    @Autowired
    private ControlActualizarInformacion controlActualizarInformacion;

    private ControlPrincipalCentro controlPrincipal;
    private Paciente pacienteSesion;

    public void inicia(Paciente paciente, ControlPrincipalCentro controlPrincipal) {
        this.pacienteSesion = paciente;
        this.controlPrincipal = controlPrincipal;
        ventana.setControlador(this);
        ventana.muestra();
        cargarAvisos();
        verificarNotificaciones();
    }

    private void cargarAvisos() {
        try {
            Aviso ultimoAviso = servicioAviso.obtenerUltimoAviso();
            String textoParaMostrar;

            if (ultimoAviso != null) {
                LocalDate fecha = ultimoAviso.getFecha();
                String contenido = ultimoAviso.getContenido();
                String fechaFormateada = "Publicado el: " + fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                textoParaMostrar = fechaFormateada + "\n\n" + contenido;
            } else {
                textoParaMostrar = "No hay avisos nuevos por el momento.";
            }
            ventana.setAvisos(textoParaMostrar);
        } catch (Exception e) {
            e.printStackTrace();
            ventana.setAvisos("No se pudieron cargar los avisos en este momento.");
        }
    }

    public Paciente getPacienteSesion() {
        return pacienteSesion;
    }

    public String getNombreUsuarioActivo() {
        return (pacienteSesion != null) ? pacienteSesion.getUsuario() : null;
    }

    public void salir() {
        if (controlPerfilPaciente != null) controlPerfilPaciente.ocultaVentana();
        if (controlActualizarInformacion != null) controlActualizarInformacion.regresa();

        ventana.oculta();
        this.pacienteSesion = null;
        if (controlPrincipal != null) {
            controlPrincipal.regresaAlLogin();
        } else {
            Platform.exit();
        }
    }
    // Método para actualizar la sesión (llamado tras guardar cambios)
    public void actualizarSesion(Paciente pacienteActualizado) {
        this.pacienteSesion = pacienteActualizado;
    }

    public void iniciarRegistroEmocional() {
        if(this.pacienteSesion != null) {
            controlRegistroEmocinal.inicia(this.pacienteSesion);
        } else {
            System.err.println("No hay paciente en sesión para el registro emocional");
        }
    }

    public void iniciarLineaCaptura() {
        if (pacienteSesion == null) {
            System.err.println("No hay paciente en sesión");
            return;
        }
        Cita citaPendiente = servicioCita.buscarCitaPendienteMasReciente(pacienteSesion);
        if (citaPendiente != null) {
            controlLineaCaptura.inicia(citaPendiente);
        } else {
            ventana.muestraAviso("Sin Pagos Pendientes", "No tienes ninguna cita pendiente de pago.");
        }
    }

    public void iniciarCrearCita() {
        if (pacienteSesion != null) {
            controlCrearCita.inicia(pacienteSesion.getUsuario());
        } else {
            System.err.println("Error: pacienteSesion es null en iniciarCrearCita()");
        }
    }

    public void iniciarListarCitas() {
        if (pacienteSesion != null) {
            controlListarCitas.inicia(pacienteSesion.getUsuario());
        } else {
            System.err.println("Error: pacienteSesion es null en iniciarListarCitas()");
        }
    }

    public void iniciarReagendarCita() {
        if (pacienteSesion != null) {
            controlReagendarCita.inicia(pacienteSesion.getUsuario());
        } else {
            System.err.println("Error: pacienteSesion es null en iniciarReagendarCita()");
        }
    }

    public void iniciarHistorialPagos() {
        if (pacienteSesion == null) {
            System.err.println("No hay paciente en sesión");
            return;
        }
        controlHistorialPagos.inicia(pacienteSesion);
    }

    public void iniciarPerfilPaciente() {
        if (pacienteSesion != null) {
            controlPerfilPaciente.inicia(pacienteSesion.getUsuario(), this);
        }
    }

    /**
     * HU-03: Escenario: Visualización de notificaciones no leídas
     * Verifica si existen notificaciones sin leer para activar la burbuja roja.
     */
    public void verificarNotificaciones() {
        if (pacienteSesion != null) {
            try {
                servicioCita.verificarCitasProximas(pacienteSesion);
                long cantidadNoLeidas = servicioNotificacion.contarNoLeidasPaciente(pacienteSesion);

                // Si existe una notificación sin leer, sale la burbuja
                ventana.setNotificacionActiva(cantidadNoLeidas > 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * HU-03: Escenario: Consultar el listado de notificaciones [cite: 25]
     * Recupera las notificaciones, las muestra en la ventana y limpia la burbuja.
     */
    public void iniciarVerNotificaciones() {
        if (pacienteSesion != null) {
            // 1. Obtener la lista ordenada (de la más próxima a la última)
            List<Notificacion> notificaciones = servicioNotificacion.obtenerTodasPorPaciente(pacienteSesion);

            // 2. Mostrar la lista en la ventana
            ventana.mostrarPanelNotificaciones(notificaciones);

            // 3. Marcar como leídas y desactivar burbuja
            servicioNotificacion.marcarTodasComoLeidasPaciente(pacienteSesion);
            ventana.setNotificacionActiva(false);
        }
    }

    // Este sí recibe el Paciente porque es el controlador nuevo que hicimos
    public void iniciarActualizarInformacion() {
        if (pacienteSesion != null) {
            controlActualizarInformacion.inicia(pacienteSesion, this);
        }
    }
}