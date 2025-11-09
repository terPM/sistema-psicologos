package mx.uam.ayd.proyecto.presentacion.psicologoPrincipal;

import javafx.application.Platform;
import mx.uam.ayd.proyecto.presentacion.registrarNotas.ControlRegistrarNotas;
import mx.uam.ayd.proyecto.presentacion.principal.ControlPrincipalCentro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ControlPsicologo {

    @Autowired
    private VentanaPsicologoPrincipal ventana;

    @Autowired
    private ControlRegistrarNotas controlRegistrarNotas; //
    private ControlPrincipalCentro controlPrincipal;

    /**
     * Inicia el flujo principal del psicólogo
     */
    public void inicia(ControlPrincipalCentro controlPrincipal) {
        this.controlPrincipal = controlPrincipal; // Almacenar la referencia
        ventana.setControlador(this);
        ventana.muestra();
    }

    public void registrarNotas() {
        controlRegistrarNotas.inicia();
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