package mx.uam.ayd.proyecto.presentacion.pacientePrincipal.perfilPaciente;

import mx.uam.ayd.proyecto.negocio.ServicioPaciente;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.ControlPaciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ControlPerfilPaciente {

    @Autowired
    private VentanaPerfilPaciente ventanaPerfilPaciente;

    @Autowired
    private ServicioPaciente servicioPaciente;

    private ControlPaciente controlPaciente;
    private Paciente pacienteActual;

    /**
     * Inicia el flujo del perfil, busca los datos y muestra la ventana.
     * @param nombreUsuario Usuario activo.
     * @param controlPaciente Referencia para el control de flujo.
     */
    public void inicia(String nombreUsuario, ControlPaciente controlPaciente) {
        this.controlPaciente = controlPaciente;
        Paciente paciente = servicioPaciente.obtenerPacientePorUsuario(nombreUsuario);
        
        if (paciente != null) {
            this.pacienteActual = paciente;
            
            ventanaPerfilPaciente.setControlador(this);
            ventanaPerfilPaciente.muestra(pacienteActual);
        } else {
            System.err.println("Error: Paciente activo no encontrado.");
        }
    }
    
    /**
     * Oculta la ventana del perfil y regresa al menú principal (ControlPaciente).
     */
    public void regresa() {
        ventanaPerfilPaciente.oculta();
        if (controlPaciente != null) {
        // Podrías agregar un log para indicar que se regresa al flujo padre
        System.out.println("Regresando del perfil al controlador: " + controlPaciente.getClass().getSimpleName()); 
        }
    }

    public void ocultaVentana() {
    ventanaPerfilPaciente.oculta();
    }
}
    