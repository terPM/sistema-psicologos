package mx.uam.ayd.proyecto.presentacion.pacientePrincipal.RegistroEmocinal;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import mx.uam.ayd.proyecto.negocio.ServicioRegistroemocional;

/**
 * Controlador para la ventana de Registro Emocional.
 */
@Component
public class ControlRegistroEmocinal {

    @Autowired
    private VentanaRegistroEmocional ventanaRegistroEmocional;

    @Autowired
    private ServicioRegistroemocional servicioRegistroEmocional;

    @PostConstruct
    public void init() {
        ventanaRegistroEmocional.setControl(this);
    }

    /**
     * Inicia el flujo mostrando la ventana.
     */
    public void inicia() {
        ventanaRegistroEmocional.muestra();
    }

    /**
     * Cierra la ventana.
     */
    public void termina() {
        ventanaRegistroEmocional.setVisible(false);
    }

    /**
     * Pide al servicio que guarde el registro.
     *
     * @param emocion La emoción seleccionada.
     * @param nota La nota opcional.
     */
    public void guardarRegistro(String emocion, String nota) {
        if (emocion == null || emocion.isEmpty()) {
            ventanaRegistroEmocional.muestraError("Debes seleccionar una emoción.");
            return;
        }

        try {
            servicioRegistroEmocional.guardarRegistro(emocion, nota);
            ventanaRegistroEmocional.muestraAviso("Éxito", "Registro guardado correctamente.");
            termina();
        } catch (IllegalArgumentException e) {
            ventanaRegistroEmocional.muestraError(e.getMessage());
        } catch (Exception e) {
            ventanaRegistroEmocional.muestraError("Error al guardar el registro: " + e.getMessage());
        }
    }
}