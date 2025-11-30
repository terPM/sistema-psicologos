package mx.uam.ayd.proyecto.presentacion.pacientePrincipal.perfilPaciente;

import mx.uam.ayd.proyecto.negocio.ServicioPaciente;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.ControlPaciente;
// Import necesario para conectar con la otra ventana
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.ActualizarInformacion.ControlActualizarInformacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ControlPerfilPaciente {

    @Autowired
    private VentanaPerfilPaciente ventanaPerfilPaciente;

    @Autowired
    private ServicioPaciente servicioPaciente;

    // Inyectamos el controlador de la OTRA ventana
    @Autowired
    private ControlActualizarInformacion controlActualizarInformacion;

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
            System.out.println("Regresando del perfil al controlador: " + controlPaciente.getClass().getSimpleName());
        }
    }

    public void ocultaVentana() {
        ventanaPerfilPaciente.oculta();
    }

    // --- NUEVO MÉTODO: Redirige a la ventana de actualización ---
    public void irAActualizarInformacion() {
        // Ocultamos la ventana actual (solo lectura)
        ocultaVentana();

        // Abrimos la ventana de edición usando el paciente actual y el control principal
        if (pacienteActual != null && controlPaciente != null) {
            controlActualizarInformacion.inicia(pacienteActual, controlPaciente);
        }
    }
    // -----------------------------------------------------------
}