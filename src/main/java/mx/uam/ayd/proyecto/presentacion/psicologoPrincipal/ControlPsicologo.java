package mx.uam.ayd.proyecto.presentacion.psicologoPrincipal;

import javafx.application.Platform;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;
import mx.uam.ayd.proyecto.presentacion.registrarNotas.ControlRegistrarNotas;
import mx.uam.ayd.proyecto.negocio.ServicioAviso;
import mx.uam.ayd.proyecto.negocio.modelo.Aviso;
import mx.uam.ayd.proyecto.presentacion.mostrarCitasPsic.ControlMostrarCitasPsic;
import mx.uam.ayd.proyecto.negocio.ServicioNotificacion;
import mx.uam.ayd.proyecto.negocio.modelo.Notificacion;
import mx.uam.ayd.proyecto.presentacion.principal.ControlPrincipalCentro;
import mx.uam.ayd.proyecto.presentacion.psicologoPrincipal.ListaRegistros.ControlListaRegistros;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ControlPsicologo {

    // --- Campos de Ambas Ramas ---
    @Autowired
    private VentanaPsicologoPrincipal ventana;
    @Autowired
    private ControlRegistrarNotas controlRegistrarNotas;
    @Autowired
    private ControlMostrarCitasPsic controlMostrarCitasPsic; // de HEAD
    @Autowired
    private ServicioAviso servicioAviso; // de HEAD
    @Autowired
    private ServicioNotificacion servicioNotificacion; // de HEAD
    @Autowired
    private ControlListaRegistros controlListaRegistros; // de hu-16
    // --- Fin de Campos ---

    private ControlPrincipalCentro controlPrincipal;

    // --- Lógica de Sesión de hu-16 (La correcta) ---
    private Psicologo psicologoSesion;

    /**
     * Método 'inicia' fusionado. Usa la firma de hu-16 y añade la lógica de HEAD.
     */
    public void inicia(ControlPrincipalCentro controlPrincipal, Psicologo psicologo) {
        this.controlPrincipal = controlPrincipal;
        this.psicologoSesion = psicologo; // Lógica de hu-16

        ventana.setControlador(this);
        ventana.muestra();

        cargarIndicadorNotificaciones(); // Lógica de HEAD
    }

    /**
     * Método alterno por compatibilidad (de HEAD).
     * NOTA: Este método puede fallar si 'psicologoActual' no se inicializa.
     */
    public void inicia(ControlPrincipalCentro controlPrincipal) {
        this.controlPrincipal = controlPrincipal;
        ventana.setControlador(this);
        ventana.muestra();
        cargarIndicadorNotificaciones();
    }

    // --- Métodos de Notificaciones (de HEAD, adaptados a psicologoSesion) ---
    public void setPsicologoActual(Psicologo psicologo) {
        this.psicologoActual = psicologo; // Método de HEAD
        if(this.psicologoSesion == null) {
            this.psicologoSesion = psicologo; // Asegura compatibilidad
        }
        cargarIndicadorNotificaciones();
    }

    public void cargarIndicadorNotificaciones() {
        // Usa la sesión de hu-16 si existe, si no, usa la de HEAD
        Psicologo psicologoACargar = (psicologoSesion != null) ? psicologoSesion : psicologoActual;

        if (psicologoACargar == null) {
            return;
        }
        List<Notificacion> noLeidas = servicioNotificacion.obtenerNoLeidas(psicologoACargar);
        ventana.setBurbujaVisible(!noLeidas.isEmpty());
    }

    public void mostrarNotificaciones() {
        Psicologo psicologoACargar = (psicologoSesion != null) ? psicologoSesion : psicologoActual;

        if (psicologoACargar == null) {
            return;
        }

        List<Notificacion> noLeidas = servicioNotificacion.obtenerNoLeidas(psicologoACargar);
        ventana.mostrarListViewNotificaciones(noLeidas);

        servicioNotificacion.marcarTodasComoLeidas(psicologoACargar);
        cargarIndicadorNotificaciones();
    }
    // --- Fin Métodos Notificaciones ---

    public void registrarNotas() {
        controlRegistrarNotas.inicia();
    }

    // --- Método de HEAD (adaptado a psicologoSesion) ---
    public void verHorario() {
        Psicologo psicologoACargar = (psicologoSesion != null) ? psicologoSesion : psicologoLogueado;

        if (psicologoACargar != null) {
            ventana.oculta();
            controlMostrarCitasPsic.inicia(this, psicologoACargar);
        } else {
            System.err.println("Error: No se ha identificado al psicólogo.");
        }
    }

    // --- Método de hu-16 ---
    public void iniciarListaRegistros() {
        if (this.psicologoSesion != null) {
            controlListaRegistros.inicia(this.psicologoSesion);
        } else {
            System.err.println("No hay psicólogo en sesión para listar registros");
        }
    }

    // --- Método de HEAD ---
    public void actualizarDisplayAviso() {
        Aviso ultimoAviso = servicioAviso.obtenerUltimoAviso();

        String textoAviso = "Aún no hay avisos publicados.";
        if (ultimoAviso != null) {
            textoAviso = "Publicado el: " + ultimoAviso.getFecha().toString() + "\n\n"
                    + ultimoAviso.getContenido();
        }

        ventana.actualizarAviso(textoAviso);
    }

    // --- Método de HEAD ---
    public void mostrarVentana() {
        ventana.muestra();
    }

    // --- Método Salir (Fusionado, usando la lógica de hu-16) ---
    public void salir() {
        ventana.oculta();
        this.psicologoSesion = null; // Limpiar sesión
        this.psicologoActual = null;
        this.psicologoLogueado = null;
        if (controlPrincipal != null) {
            controlPrincipal.regresaAlLogin();
        } else {
            Platform.exit();
        }
    }

    // --- Campos de compatibilidad de HEAD (se pueden borrar si 'psicologoSesion' se usa en todos lados) ---
    private Psicologo psicologoActual;
    private Psicologo psicologoLogueado;
}