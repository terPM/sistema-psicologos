package mx.uam.ayd.proyecto.presentacion.psicologoPrincipal;

import javafx.application.Platform;
import mx.uam.ayd.proyecto.presentacion.registrarNotas.ControlRegistrarNotas;
import mx.uam.ayd.proyecto.negocio.ServicioAviso;
import mx.uam.ayd.proyecto.negocio.modelo.Aviso;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;
import mx.uam.ayd.proyecto.presentacion.mostrarCitasPsic.ControlMostrarCitasPsic;
import mx.uam.ayd.proyecto.negocio.ServicioNotificacion;
import mx.uam.ayd.proyecto.negocio.modelo.Notificacion;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;
import mx.uam.ayd.proyecto.presentacion.principal.ControlPrincipalCentro;
import mx.uam.ayd.proyecto.presentacion.registrarNotas.ControlRegistrarNotas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ControlPsicologo {

    @Autowired
    private VentanaPsicologoPrincipal ventana;

    @Autowired
    private ControlRegistrarNotas controlRegistrarNotas;

    @Autowired 
    private ControlMostrarCitasPsic controlMostrarCitasPsic;

    @Autowired 
    private ServicioAviso servicioAviso;
    @Autowired
    private ServicioNotificacion servicioNotificacion;

    private ControlPrincipalCentro controlPrincipal;
    private Psicologo psicologoActual;


    private Psicologo psicologoLogueado;

    /**
     * Úsalo cuando YA tienes al psicólogo logueado.
     */
    public void inicia(ControlPrincipalCentro controlPrincipal, Psicologo psicologo) { // <-- 1. MÉTODO MODIFICADO
        this.controlPrincipal = controlPrincipal; 
        this.psicologoLogueado = psicologo;     
    public void inicia(Psicologo psicologo, ControlPrincipalCentro controlPrincipal) {
        this.psicologoActual = psicologo; // <- muy importante para saber a quién mostrarle notificaciones
        this.controlPrincipal = controlPrincipal;

        ventana.setControlador(this);
        ventana.muestra();

        // pinta la burbuja si hay no leídas
        cargarIndicadorNotificaciones();
    }

    // (mantengo también el inicia original por compatibilidad, si lo necesitas)
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
        if (psicologoActual == null) return;
        List<Notificacion> noLeidas = servicioNotificacion.obtenerNoLeidas(psicologoActual);
        ventana.setBurbujaVisible(!noLeidas.isEmpty());
    }

    public void mostrarNotificaciones() {
        if (psicologoActual == null) return;

        List<Notificacion> noLeidas = servicioNotificacion.obtenerNoLeidas(psicologoActual);
        ventana.mostrarListViewNotificaciones(noLeidas);

        // marcarlas como leídas y refrescar burbuja
        servicioNotificacion.marcarTodasComoLeidas(psicologoActual);
        cargarIndicadorNotificaciones();
    }

    public void registrarNotas() {
        controlRegistrarNotas.inicia();
    }

    public void verHorario() {
        if (psicologoLogueado != null) {
            // Oculta la ventana principal del psicólogo
            ventana.oculta();
            // Inicia el controlador de citas, pasando el psicólogo
            controlMostrarCitasPsic.inicia(this, this.psicologoLogueado);
        } else {
            // Manejar caso de error (aunque no debería pasar si inicia() se llama bien)
            System.err.println("Error: No se ha identificado al psicólogo.");
        }
    }

    public void actualizarDisplayAviso() { 
        Aviso ultimoAviso = servicioAviso.obtenerUltimoAviso();
        
        String textoAviso = "Aún no hay avisos publicados.";
        
        if (ultimoAviso != null) {
            textoAviso = "Publicado el: " + ultimoAviso.getFecha().toString() + "\n\n" 
                       + ultimoAviso.getContenido();
        }
        
        // Llama al método en la ventana para actualizar el texto
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
