package mx.uam.ayd.proyecto.presentacion.psicologoPrincipal;

import javafx.application.Platform;
import mx.uam.ayd.proyecto.presentacion.registrarNotas.ControlRegistrarNotas;
import mx.uam.ayd.proyecto.negocio.ServicioAviso;
import mx.uam.ayd.proyecto.negocio.modelo.Aviso;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;
import mx.uam.ayd.proyecto.presentacion.mostrarCitasPsic.ControlMostrarCitasPsic;
import mx.uam.ayd.proyecto.presentacion.principal.ControlPrincipalCentro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    private ControlPrincipalCentro controlPrincipal;


    private Psicologo psicologoLogueado;

    /**
     * Inicia el flujo principal del psicólogo
     */
    public void inicia(ControlPrincipalCentro controlPrincipal, Psicologo psicologo) { // <-- 1. MÉTODO MODIFICADO
        this.controlPrincipal = controlPrincipal; 
        this.psicologoLogueado = psicologo;     
        ventana.setControlador(this);
        ventana.muestra();
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
        ventana.oculta(); // Oculta la ventana actual del psicólogo
        if (controlPrincipal != null) {
            controlPrincipal.regresaAlLogin(); // Llama al método de ControlPrincipalCentro
        } else {
            Platform.exit(); // Fallback por si la referencia es nula
        }
    }
}