package mx.uam.ayd.proyecto.presentacion.listarpacientes;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import mx.uam.ayd.proyecto.negocio.ServicioBateriaClinica;
import mx.uam.ayd.proyecto.negocio.ServicioPaciente;
import mx.uam.ayd.proyecto.negocio.modelo.BateriaClinica;
import mx.uam.ayd.proyecto.negocio.modelo.HistorialClinico;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo; // IMPORTADO
import mx.uam.ayd.proyecto.presentacion.asignarPsicologo.ControlAsignarPsicologo; // IMPORTADO

/**
 * Controlador para la funcionalidad de listar pacientes.
 * (Documentación...)
 */
@Component
public class ControlListarPacientes {

    @Autowired
    private VentanaListarPacientes ventana;

    @Autowired
    private ServicioPaciente servicioPaciente;

    @Autowired
    private ServicioBateriaClinica servicioBateriaClinica;

    // --- CÓDIGO AÑADIDO ---
    @Autowired
    private ControlAsignarPsicologo controlAsignarPsicologo;
    // --- FIN DEL CÓDIGO AÑADIDO ---

    /**
     * Inicia la vista de listado de pacientes. (Modo Admin)
     * (Documentación...)
     */
    public void inicia() {
        List<Paciente> todosLosPacientes = servicioPaciente.recuperarTodosLosPacientes();
        ventana.muestra(this, todosLosPacientes);
    }

    /**
     * Inicia la vista de listado de pacientes (MODO PSICÓLOGO).
     * Muestra SOLO los pacientes asignados al psicólogo.
     *
     * @param psicologo El psicólogo que ha iniciado sesión.
     */
    public void inicia(Psicologo psicologo) {
        if (psicologo == null) {
            ventana.muestraDialogoDeError("Error: No se pudo identificar al psicólogo.");
            return;
        }
        List<Paciente> pacientesAsignados = servicioPaciente.recuperarPacientesPorPsicologo(psicologo);
        ventana.muestra(this, pacientesAsignados);
    }

    /**
     * Maneja la selección de un paciente en la vista.
     * (Documentación...)
     */
    public void seleccionarPaciente(Paciente paciente) {
        ventana.limpiarDetallesDeBateria();
        ventana.limpiarHistorialEnPestana();

        if (paciente != null) {
            ventana.mostrarBaterias(paciente.getBateriasClinicas());

            HistorialClinico historial = paciente.getHistorialClinico();
            if (historial != null) {
                ventana.mostrarHistorialEnPestana(historial);
            }
        }
    }

    /**
     * Se invoca cuando se selecciona una batería clínica.
     * (Documentación...)
     */
    public void seleccionarBateria(BateriaClinica bateria) {
        if (bateria != null) {
            ventana.mostrarDetallesBateria(bateria);
        }
    }

    /**
     * Obtiene una cadena formateada con los detalles de una batería.
     * (Documentación...)
     */
    public void abrirDetallesBateria(BateriaClinica bateria) {
        String detalles = servicioBateriaClinica.obtenerDetallesBateria(bateria);
        ventana.muestraDialogoDeInformacion(detalles);
    }

    /**
     * Guarda los comentarios que el usuario ha escrito.
     * (Documentación...)
     */
    public void guardarComentarios(BateriaClinica bateria, String comentarios) {
        if (bateria != null) {
            try {
                servicioBateriaClinica.guardarComentarios(bateria, comentarios);
                ventana.muestraDialogoDeInformacion("Comentarios guardados con éxito.");
            } catch (Exception e) {
                ventana.muestraDialogoDeError("Error al guardar: " + e.getMessage());
            }
        } else {
            ventana.muestraDialogoDeError("No hay una batería seleccionada para guardar comentarios.");
        }
    }

    // --- CÓDIGO AÑADIDO ---
    /**
     * Inicia el flujo para asignar un psicólogo al paciente seleccionado.
     * @param paciente El paciente seleccionado en la tabla.
     */
    public void asignarPsicologo(Paciente paciente) {
        controlAsignarPsicologo.inicia(paciente);
    }
    // --- FIN DEL CÓDIGO AÑADIDO ---

    /**
     * Cierra la ventana de listado de pacientes.
     */
    public void cerrar() {
        ventana.cierra();
    }
}