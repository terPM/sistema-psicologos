package mx.uam.ayd.proyecto.presentacion.pacientePrincipal.RegistroEmocinal;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.negocio.ServicioRegistroemocional;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;

@Component
public class ControlRegistroEmocinal {

    @Autowired
    private VentanaRegistroEmocional ventanaRegistroEmocional;

    @Autowired
    private ServicioRegistroemocional servicioRegistroEmocional;

    private Paciente pacienteActual;

    @PostConstruct
    public void init() {
        ventanaRegistroEmocional.setControl(this);
    }

    /**
     * Inicia el flujo mostrando la ventana.
     * Recibe al paciente que va a hacer el registro.
     */
    public void inicia(Paciente paciente) {
        this.pacienteActual = paciente; // Guardamos al paciente
        ventanaRegistroEmocional.muestra();
    }

    /**
     * Cierra la ventana.
     */
    public void termina() {
        this.pacienteActual = null; // Limpiamos al paciente al cerrar
        ventanaRegistroEmocional.setVisible(false);
    }

    /**
     * Pide al servicio que guarde el registro.
     */
    public void guardarRegistro(String emocion, String nota) {
        if (emocion == null || emocion.isEmpty()) {
            ventanaRegistroEmocional.muestraError("Debes seleccionar una emoción.");
            return;
        }

        Paciente pacienteLogueado = this.pacienteActual;

        if (pacienteLogueado == null) {
            ventanaRegistroEmocional.muestraError("Error de sesión. No se pudo identificar al paciente. Por favor, reinicie la aplicación.");
            return;
        }

        try {
            servicioRegistroEmocional.guardarRegistro(emocion, nota, pacienteLogueado);

            ventanaRegistroEmocional.muestraAviso("Éxito", "Registro guardado correctamente.");
            termina();
        } catch (IllegalArgumentException e) {
            ventanaRegistroEmocional.muestraError(e.getMessage());
        } catch (Exception e) {
            ventanaRegistroEmocional.muestraError("Error al guardar el registro: " + e.getMessage());
        }
    }
}