package mx.uam.ayd.proyecto.presentacion.pacientePrincipal;

import javafx.application.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Lazy;

import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.RegistroEmocinal.ControlRegistroEmocinal;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.lineaCaptura.ControlLineaCaptura;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.ListaRegistros.ControlListaRegistros;
import mx.uam.ayd.proyecto.presentacion.principal.ControlPrincipalCentro;
import mx.uam.ayd.proyecto.presentacion.crearCita.ControlCrearCita;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.ListarCitas.ControlListarCitas;

@Component
public class ControlPaciente {

    @Autowired
    private VentanaPacientePrincipal ventana;
    @Autowired
    private ControlRegistroEmocinal controlRegistroEmocinal;
    @Autowired
    private ControlListaRegistros controlListaRegistros;
    @Autowired
    @Lazy
    private ControlLineaCaptura controlLineaCaptura;
    @Autowired
    private ControlCrearCita controlCrearCita;
    @Autowired
    private ControlListarCitas controlListarCitas;

    private ControlPrincipalCentro controlPrincipal;    
    private String nombreUsuarioActivo;

    /**
     * Inicia el flujo principal del paciente, 
     * recibiendo y guardando el nombre de usuario de la sesión.
     * @param nombreUsuarioActivo El nombre de usuario que ingresó en el login.
     * @param controlPrincipal La referencia al controlador principal para la salida.
     */
    public void inicia(String nombreUsuarioActivo, ControlPrincipalCentro controlPrincipal) { 
        this.nombreUsuarioActivo = nombreUsuarioActivo;
        this.controlPrincipal = controlPrincipal; 
        ventana.setControlador(this);
        ventana.muestra();
    }

    /**
     * Devuelve el nombre del usuario activo (obtenido del login).
     */
    public String getNombreUsuarioActivo() { 
        return nombreUsuarioActivo;
    }

    /**
     * Cierra la aplicación
     */
    public void salir() {
        ventana.oculta(); // Oculta la ventana actual del paciente
        if (controlPrincipal != null) {
            controlPrincipal.regresaAlLogin(); // Llama al método de ControlPrincipalCentro
        } else {
            Platform.exit(); // Fallback por si la referencia es nula
        }
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

    /**
     * Inicia el sub-flujo de Generar Línea de Captura.
     */
    public void iniciarLineaCaptura() { 
        if (nombreUsuarioActivo != null) {
            controlLineaCaptura.inicia(); 
        }
    }
    /**
     * Inicia el sub-flujo de Crear Cita.
     */
    public void iniciarCrearCita(){
            controlCrearCita.inicia(nombreUsuarioActivo);
    }

    public void iniciarListarCitas() {
        controlListarCitas.inicia();
    }

}