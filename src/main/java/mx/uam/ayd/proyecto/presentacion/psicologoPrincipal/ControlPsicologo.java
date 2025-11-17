package mx.uam.ayd.proyecto.presentacion.psicologoPrincipal;

import javafx.application.Platform;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;
import mx.uam.ayd.proyecto.presentacion.registrarNotas.ControlRegistrarNotas;
import mx.uam.ayd.proyecto.negocio.ServicioAviso;
import mx.uam.ayd.proyecto.negocio.modelo.Aviso;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;
import mx.uam.ayd.proyecto.presentacion.mostrarCitasPsic.ControlMostrarCitasPsic;
import mx.uam.ayd.proyecto.negocio.ServicioNotificacion;
import mx.uam.ayd.proyecto.negocio.modelo.Notificacion;
import mx.uam.ayd.proyecto.presentacion.principal.ControlPrincipalCentro;
<<<<<<< HEAD
=======
import mx.uam.ayd.proyecto.presentacion.psicologoPrincipal.ListaRegistros.ControlListaRegistros;

>>>>>>> hu-16-historial-de-pagos
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ControlPsicologo {

    @Autowired
    private VentanaPsicologoPrincipal ventana;

    @Autowired
    private ControlRegistrarNotas controlRegistrarNotas;

<<<<<<< HEAD
    @Autowired
    private ControlMostrarCitasPsic controlMostrarCitasPsic;

    @Autowired
    private ServicioAviso servicioAviso;

    @Autowired
    private ServicioNotificacion servicioNotificacion;

=======
>>>>>>> hu-16-historial-de-pagos
    private ControlPrincipalCentro controlPrincipal;
    private Psicologo psicologoActual;
    private Psicologo psicologoLogueado;

    @Autowired
    private ControlListaRegistros controlListaRegistros;

    private Psicologo psicologoSesion;

    /**
     * Método usado cuando ya tienes al psicólogo logueado.
     */
    public void inicia(ControlPrincipalCentro controlPrincipal, Psicologo psicologo) {
        this.controlPrincipal = controlPrincipal;
<<<<<<< HEAD
        this.psicologoLogueado = psicologo;
        this.psicologoActual = psicologo;

=======
        this.psicologoSesion = psicologo;
>>>>>>> hu-16-historial-de-pagos
        ventana.setControlador(this);
        ventana.muestra();

        cargarIndicadorNotificaciones();
    }

    /**
     * Método alterno por compatibilidad.
     */
    public void inicia(ControlPrincipalCentro controlPrincipal) {
        this.controlPrincipal = controlPrincipal;

        ventana.setControlador(this);
        ventana.muestra();

        cargarIndicadorNotificaciones();
    }

    public void setPsicologoActual(Psicologo psicologo) {
        this.psicologoActual = psicologo;
        cargarIndicadorNotificaciones();
    }

    public void cargarIndicadorNotificaciones() {
        if (psicologoActual == null) {
            return;
        }
        List<Notificacion> noLeidas = servicioNotificacion.obtenerNoLeidas(psicologoActual);
        ventana.setBurbujaVisible(!noLeidas.isEmpty());
    }

    public void mostrarNotificaciones() {
        if (psicologoActual == null) {
            return;
        }

        List<Notificacion> noLeidas = servicioNotificacion.obtenerNoLeidas(psicologoActual);
        ventana.mostrarListViewNotificaciones(noLeidas);

        servicioNotificacion.marcarTodasComoLeidas(psicologoActual);
        cargarIndicadorNotificaciones();
    }

    public void registrarNotas() {
        controlRegistrarNotas.inicia();
    }

<<<<<<< HEAD
    public void verHorario() {
        if (psicologoLogueado != null) {
            ventana.oculta();
            controlMostrarCitasPsic.inicia(this, this.psicologoLogueado);
        } else {
            System.err.println("Error: No se ha identificado al psicólogo.");
=======
    /**
     * Inicia el flujo para ver la lista de registros
     */
    public void iniciarListaRegistros() {
        if (this.psicologoSesion != null) {
            controlListaRegistros.inicia(this.psicologoSesion);
        } else {
            System.err.println("No hay psicólogo en sesión para listar registros");
        }
    }

    public void salir() {
        ventana.oculta();
        this.psicologoSesion = null;
        if (controlPrincipal != null) {
            controlPrincipal.regresaAlLogin();
        } else {
            Platform.exit();
>>>>>>> hu-16-historial-de-pagos
        }
    }

    public void actualizarDisplayAviso() {
        Aviso ultimoAviso = servicioAviso.obtenerUltimoAviso();

        String textoAviso = "Aún no hay avisos publicados.";
        if (ultimoAviso != null) {
            textoAviso = "Publicado el: " + ultimoAviso.getFecha().toString() + "\n\n"
                    + ultimoAviso.getContenido();
        }

        ventana.actualizarAviso(textoAviso);
    }

    public void mostrarVentana() {
        ventana.muestra();
    }

    public void salir() {
        ventana.oculta();
        if (controlPrincipal != null) {
            controlPrincipal.regresaAlLogin();
        } else {
            Platform.exit();
        }
    }
}
