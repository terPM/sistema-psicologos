package mx.uam.ayd.proyecto.presentacion.psicologoPrincipal;

import javafx.application.Platform;
import mx.uam.ayd.proyecto.presentacion.registrarNotas.ControlRegistrarNotas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ControlPsicologo {

    @Autowired
    private VentanaPsicologoPrincipal ventana;

    @Autowired
    private ControlRegistrarNotas controlRegistrarNotas; //
    /**
     * Inicia el flujo principal del psicólogo
     */
    public void inicia() {
        ventana.setControlador(this);
        ventana.muestra();
    }
    public void registrarNotas() {
        controlRegistrarNotas.inicia();
    }

    public void salir() {
        Platform.exit();
    }
}