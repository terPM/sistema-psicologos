package mx.uam.ayd.proyecto.presentacion.pacientePrincipal.EncuestaSatisfaccion;

import mx.uam.ayd.proyecto.negocio.ServicioEncuestaSatisfaccion;
import mx.uam.ayd.proyecto.negocio.modelo.EncuestaSatisfaccion;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Controlador de Lógica de Negocio para la Encuesta de Satisfacción.
 */
@Component
public class ControlEncuestaSatisfaccion {

    @Autowired
    private ServicioEncuestaSatisfaccion servicioEncuestaSatisfaccion;

    @Autowired
    @Lazy
    private VentanaEncuestaSatisfaccion ventana;

    private EncuestaSatisfaccion encuestaActual;
    private Paciente pacienteActual; 
    private Runnable callbackEncuestaTerminada;

    /**
     * Inicializa una nueva instancia de encuesta con el contexto del paciente
     * y registra una acción de callback a ejecutar al finalizar con éxito.
     * @param paciente El paciente que está contestando la encuesta.
     * @param onCompletion El código a ejecutar al completar exitosamente la encuesta.
     */
    public void iniciaEncuesta(Paciente paciente, Runnable onCompletion) { 
        this.pacienteActual = paciente;
        this.callbackEncuestaTerminada = onCompletion; 
        this.encuestaActual = new EncuestaSatisfaccion();
        ventana.muestra(); 
    }
    
    /**
     * Cierra la ventana y limpia el estado de la sesión de encuesta.
     */
    private void termina() {
        this.encuestaActual = null;
        this.pacienteActual = null;
        this.callbackEncuestaTerminada = null;
        ventana.cerrarVentana();
    }
    
    /**
     * Maneja la acción del botón "Cancelar".
     */
    public void terminarEncuesta() {
        System.out.println("Control: Encuesta cancelada.");
        termina();
    }

    /**
     * Recibe todas las respuestas de la Vista, realiza la validación de negocio
     * y persiste la encuesta.
     * * @param q1Val Valor de Q1 (1-4).
     * @param q7Val ...hasta Q7.
     * @param q8 Texto de mejora.
     * @param q9 Texto de comentarios.
     */
    public void enviarEncuesta(int q1Val, int q2Val, int q3Val, int q4Val, int q5Val, int q6Val, int q7Val, String q8, String q9) {
        
        if (pacienteActual == null) {
            ventana.muestraError("Error de sesión. No se pudo identificar al paciente.");
            termina();
            return;
        }
        
        if (q1Val == 0 || q2Val == 0 || q3Val == 0 || q4Val == 0 || q5Val == 0 || q6Val == 0 || q7Val == 0) {
            ventana.muestraError("Debe contestar todas las preguntas de calificación (1 a 7) antes de guardar.");
            return; 
        }
        
        encuestaActual.setQ1Empatia(q1Val);
        encuestaActual.setQ2Confianza(q2Val);
        encuestaActual.setQ3Respeto(q3Val);
        encuestaActual.setQ4Confidencialidad(q4Val);
        encuestaActual.setQ5Herramientas(q5Val);
        encuestaActual.setQ6Satisfaccion(q6Val);
        encuestaActual.setQ7Recomendacion(q7Val);
        
        encuestaActual.setQ8Mejora(q8.trim());
        encuestaActual.setQ9Comentarios(q9.trim());
        encuestaActual.setPaciente(pacienteActual);
        
        try {
            servicioEncuestaSatisfaccion.guardarEncuesta(encuestaActual);             
            ventana.muestraAviso("Éxito", "Encuesta guardada correctamente.");

            // Deshabilitar botón en la ventana principal (si aplica)
            if (callbackEncuestaTerminada != null) {
                callbackEncuestaTerminada.run();
            }
            
            termina();

        } catch (IllegalArgumentException e) {
             ventana.muestraError(e.getMessage());
        } catch (Exception e) {
            ventana.muestraError("Error al guardar la encuesta: " + e.getMessage());
        }
    }
}
