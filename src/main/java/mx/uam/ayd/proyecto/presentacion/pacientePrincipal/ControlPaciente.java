package mx.uam.ayd.proyecto.presentacion.pacientePrincipal;

import javafx.application.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.RegistroEmocinal.ControlRegistroEmocinal;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.ListaRegistros.ControlListaRegistros;


@Component
public class ControlPaciente {

    @Autowired
    private VentanaPacientePrincipal ventana;

    @Autowired
    private ControlRegistroEmocinal controlRegistroEmocinal;

    @Autowired
    private ControlListaRegistros controlListaRegistros;

    /**
     * Inicia el flujo principal del paciente
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

    /**
     * Inicia el sub-flujo de Registro Emocional
     */
    public void iniciarRegistroEmocional() {
        controlRegistroEmocinal.inicia();
    }

    /**
     * Inicia el sub-flujo de Lista de Registros
     */
    public void iniciarListaRegistros() {
        controlListaRegistros.inicia();
    }
}