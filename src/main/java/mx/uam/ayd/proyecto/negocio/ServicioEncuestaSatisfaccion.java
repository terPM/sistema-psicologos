package mx.uam.ayd.proyecto.negocio;

import mx.uam.ayd.proyecto.datos.EncuestaSatisfaccionRepository;
import mx.uam.ayd.proyecto.negocio.modelo.EncuestaSatisfaccion;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.ControlPaciente; // NUEVO: Importamos el controlador
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Servicio de Negocio para manejar la lógica y persistencia de las Encuestas de Satisfacción,
 * incluyendo el control del estado de habilitación global de la encuesta.
 */
@Service
public class ServicioEncuestaSatisfaccion {

    @Autowired
    private EncuestaSatisfaccionRepository encuestaSatisfaccionRepository;
    private static boolean encuestaHabilitada = false;
    
    // NUEVO: Referencia al controlador del paciente activo (listener)
    private ControlPaciente pacienteListener = null; 

    /**
     * Registra el controlador del paciente que necesita escuchar el estado.
     */
    public void registrarPacienteListener(ControlPaciente controlPaciente) {
        this.pacienteListener = controlPaciente;
    }
    
    /**
     * Elimina el registro del controlador (al cerrar sesión).
     */
    public void desregistrarPacienteListener() {
        this.pacienteListener = null;
    }

    /**
     * Habilita la encuesta de satisfacción. Llamado por el administrador.
     */
    public void habilitarEncuesta() {
        if (!encuestaHabilitada) { // Solo si no estaba habilitada
            encuestaHabilitada = true;
            System.out.println("ServicioEncuestaSatisfaccion: Encuesta habilitada globalmente.");
            
            // NUEVO: Notificar al paciente activo para actualizar su botón
            if (pacienteListener != null) {
                pacienteListener.actualizarEstadoEncuesta();
            }
        }
    }

    /**
     * Deshabilita la encuesta de satisfacción.
     */
    public void deshabilitarEncuesta() {
        encuestaHabilitada = false;
        System.out.println("ServicioEncuestaSatisfaccion: Encuesta deshabilitada globalmente.");
    }

    /**
     * Verifica si la encuesta está habilitada para ser contestada por los pacientes.
     * Llamado por ControlPaciente.
     * @return true si la encuesta está habilitada, false en caso contrario.
     */
    public boolean isEncuestaHabilitada() {
        return encuestaHabilitada;
    }

    /**
     * Persiste una instancia completa de la Encuesta de Satisfacción en la base de datos.
     * @param encuesta La entidad EncuestaSatisfaccion con todas las respuestas recopiladas.
     * @return La entidad guardada con su ID generado por la BD.
     */
    public EncuestaSatisfaccion guardarEncuesta(EncuestaSatisfaccion encuesta) {        
        if (encuesta.getFechaHora() == null) {
            encuesta.setFechaHora(LocalDateTime.now());
        }
        return encuestaSatisfaccionRepository.save(encuesta);
    }

    /**
     * Recupera todas las encuestas de satisfacción registradas.
     * @return Una lista de todas las EncuestaSatisfaccion.
     */
    public List<EncuestaSatisfaccion> obtenerTodasLasEncuestas() {
        return StreamSupport.stream(encuestaSatisfaccionRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }
    
    /**
     * Recupera todas las encuestas respondidas después de una fecha y hora específicas.
     * @param fechaHora La marca de tiempo inicial para buscar.
     * @return Lista de encuestas que cumplen el criterio.
     */
    public List<EncuestaSatisfaccion> buscarEncuestasDesdeFecha(LocalDateTime fechaHora) {
        return encuestaSatisfaccionRepository.findByFechaHoraAfter(fechaHora);
    }
    
    /**
     * Recupera encuestas donde la calificación de satisfacción (Q6) cae dentro de un rango.
     * @param minScore Calificación mínima (1-4).
     * @param maxScore Calificación máxima (1-4).
     * @return Lista de encuestas que cumplen con el rango de Q6.
     */
    public List<EncuestaSatisfaccion> buscarEncuestasPorRangoSatisfaccion(int minScore, int maxScore) {
        return encuestaSatisfaccionRepository.findByQ6SatisfaccionBetween(minScore, maxScore);
    }

    /**
     * Obtiene solo los textos de la respuesta Q8 (Mejora), excluyendo vacíos/nulos.
     */
    public List<String> obtenerTextosQ8() {
        return obtenerTodasLasEncuestas().stream()
                .map(EncuestaSatisfaccion::getQ8Mejora)
                .filter(s -> s != null && !s.trim().isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Obtiene solo los textos de la respuesta Q9 (Comentarios), excluyendo vacíos/nulos.
     */
    public List<String> obtenerTextosQ9() {
        return obtenerTodasLasEncuestas().stream()
                .map(EncuestaSatisfaccion::getQ9Comentarios)
                .filter(s -> s != null && !s.trim().isEmpty())
                .collect(Collectors.toList());
    }
}