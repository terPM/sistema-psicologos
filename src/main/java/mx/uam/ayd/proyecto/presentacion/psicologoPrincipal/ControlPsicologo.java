package mx.uam.ayd.proyecto.presentacion.psicologoPrincipal;

import javafx.application.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ControlPsicologo {

    @Autowired
    private VentanaPsicologoPrincipal ventana;

    /**
     * Inicia el flujo principal del psicólogo
     */
    public void inicia() {
        ventana.setControlador(this);
        ventana.muestra();
    }

    /**
     * Cierra la aplicación
     */
    public void salir() {
        Platform.exit();
    }
}