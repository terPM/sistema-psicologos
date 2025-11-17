package mx.uam.ayd.proyecto.presentacion.pacientePrincipal;

import javafx.application.Platform;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.reagendarCita.ControlReagendarCita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Lazy;

// Imports de Ambas Ramas
import mx.uam.ayd.proyecto.negocio.ServicioAviso;
import mx.uam.ayd.proyecto.negocio.modelo.Aviso;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import mx.uam.ayd.proyecto.negocio.ServicioCita;
import mx.uam.ayd.proyecto.negocio.modelo.Cita;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
// --- Fin de Imports ---

import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.RegistroEmocinal.ControlRegistroEmocinal;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.lineaCaptura.ControlLineaCaptura;
import mx.uam.ayd.proyecto.presentacion.principal.ControlPrincipalCentro;
import mx.uam.ayd.proyecto.presentacion.crearCita.ControlCrearCita;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.ListarCitas.ControlListarCitas;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.HistorialPagos.ControlHistorialPagos;
// Se eliminó la importación de 'ControlListaRegistros'
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.perfilPaciente.ControlPerfilPaciente;

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

    // --- CAMPO ELIMINADO ---
    // @Autowired
    // private ControlListaRegistros controlListaRegistros; // <- Eliminado

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
    // --- Fin de Campos ---

    private ControlPrincipalCentro controlPrincipal;

    private Paciente pacienteSesion;

    public void inicia(Paciente paciente, ControlPrincipalCentro controlPrincipal) {
        this.pacienteSesion = paciente;
        this.controlPrincipal = controlPrincipal;
        ventana.setControlador(this);
        ventana.muestra();
        cargarAvisos(); // Lógica de HEAD
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
        if (controlPerfilPaciente != null) {
            controlPerfilPaciente.ocultaVentana();
        }
        ventana.oculta();
        this.pacienteSesion = null;
        if (controlPrincipal != null) {
            controlPrincipal.regresaAlLogin();
        } else {
            Platform.exit();
        }
    }

    public void iniciarRegistroEmocional() {
        if(this.pacienteSesion != null) {
            controlRegistroEmocinal.inicia(this.pacienteSesion);
        } else {
            System.err.println("No hay paciente en sesión para el registro emocional");
        }
    }

    // --- MÉTODO ELIMINADO ---
    // public void iniciarListaRegistros() { ... }
    // --- FIN DE ELIMINACIÓN ---

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
}