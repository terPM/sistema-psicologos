package mx.uam.ayd.proyecto.presentacion.mostrarCitasPsic;

import mx.uam.ayd.proyecto.negocio.ServicioCita; 
import mx.uam.ayd.proyecto.negocio.modelo.Cita;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;
import mx.uam.ayd.proyecto.presentacion.psicologoPrincipal.ControlPsicologo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Controlador para la ventana que muestra el horario de citas.
 * Se encarga de obtener las citas y pasarlas a la ventana.
 */
@Component
public class ControlMostrarCitasPsic {

    @Autowired
    private VentanaMostrarCitas ventana;

    @Autowired
    private ServicioCita servicioCita; 

    private ControlPsicologo controlPsicologo; 

    /**
     * Inicia el flujo de mostrar el horario.
     *
     * @param controlPsicologo El controlador principal para poder regresar.
     */
  public void inicia(ControlPsicologo controlPsicologo, Psicologo psicologo) {
        this.controlPsicologo = controlPsicologo;

        List<Cita> citasDelPsicologo = servicioCita.obtenerCitasPorPsicologo(psicologo);
        ventana.setControlador(this);
        ventana.muestra(citasDelPsicologo);
    }

    /**
     * Maneja el evento de clic en una cita específica (llamado desde la ventana).
     *
     * @param cita La cita que fue seleccionada.
     */
    public void citaSeleccionada(Cita cita) {
        System.out.println("Se hizo clic en la cita de: " + cita.getPaciente().getNombre());
        // Aquí podrías navegar a otra ventana, por ejemplo:
        // controlRegistrarNotas.inicia(cita); 
    }

    /**
     * Regresa a la ventana principal del psicólogo.
     */
public void volver() {
        ventana.oculta(); 
        controlPsicologo.mostrarVentana(); 
    }
}