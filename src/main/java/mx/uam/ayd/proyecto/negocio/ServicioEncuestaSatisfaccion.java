package mx.uam.ayd.proyecto.negocio;

import mx.uam.ayd.proyecto.datos.EncuestaSatisfaccionRepository;
import mx.uam.ayd.proyecto.negocio.modelo.EncuestaSatisfaccion;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente; // 1. IMPORTACIÓN NECESARIA
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.ControlPaciente; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional; // Necesario para buscar
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
    
    // ... (Métodos registrar/desregistrar/habilitar/deshabilitar existentes)

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

    // 2. MÉTODO AGREGADO: Criterio Restricción de responder nuevamente
    /**
     * Verifica si un paciente ya ha respondido la encuesta.
     * Este método permite que la prueba unitaria lo simule (@Spy).
     * @param paciente El paciente a verificar.
     * @return true si ya existe una encuesta para este paciente.
     */
    public boolean yaRespondio(Paciente paciente) {
        // En un escenario real, DEBERÍAS usar un método del repositorio 
        // optimizado (ej: encuestaSatisfaccionRepository.existsByPaciente(paciente)).
        
        // Aquí usamos la implementación menos eficiente para no modificar el Repository:
        Optional<EncuestaSatisfaccion> encuestaExistente = StreamSupport.stream(encuestaSatisfaccionRepository.findAll().spliterator(), false)
            // Asumiendo que getPaciente() existe en EncuestaSatisfaccion para comparar
            .filter(e -> e.getPaciente() != null && e.getPaciente().equals(paciente)) 
            .findFirst();

        return encuestaExistente.isPresent();
    }


    /**
     * Persiste una instancia completa de la Encuesta de Satisfacción en la base de datos.
     * Criterio: Al responder la encuesta de satisfacción.
     * @param encuesta La entidad EncuestaSatisfaccion con todas las respuestas recopiladas.
     * @return La entidad guardada con su ID generado por la BD.
     * @throws IllegalStateException Si faltan preguntas obligatorias (para el test de error).
     */
    public EncuestaSatisfaccion guardarEncuesta(EncuestaSatisfaccion encuesta) throws IllegalStateException {        
        
        // 3. LÓGICA AGREGADA: Criterio Faltan preguntas obligatorias (1-7)
        if (!validarRespuestasObligatorias(encuesta)) {
            // Esto cumple el criterio "veré un mensaje de error" (a través de la excepción)
            throw new IllegalStateException("Debe responder todas las preguntas obligatorias (1-7).");
        }

        if (encuesta.getFechaHora() == null) {
            encuesta.setFechaHora(LocalDateTime.now());
        }
        return encuestaSatisfaccionRepository.save(encuesta);
    }
    
    // 4. LÓGICA AGREGADA: Auxiliar de validación
    /**
     * Lógica para verificar que las preguntas 1 a 7 tienen una respuesta válida (mayor a 0, asumiendo 1-4).
     */
    private boolean validarRespuestasObligatorias(EncuestaSatisfaccion encuesta) {
        return encuesta.getQ1Empatia() > 0 && 
               encuesta.getQ2Confianza() > 0 && 
               encuesta.getQ3Respeto() > 0 && 
               encuesta.getQ4Confidencialidad() > 0 && 
               encuesta.getQ5Herramientas() > 0 && 
               encuesta.getQ6Satisfaccion() > 0 && 
               encuesta.getQ7Recomendacion() > 0;
    }

    // ... (Resto de los métodos obtenerTodasLasEncuestas, buscarEncuestasDesdeFecha, etc., existentes)
    
    public List<EncuestaSatisfaccion> obtenerTodasLasEncuestas() {
        return StreamSupport.stream(encuestaSatisfaccionRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }
    
    public List<EncuestaSatisfaccion> buscarEncuestasDesdeFecha(LocalDateTime fechaHora) {
        return encuestaSatisfaccionRepository.findByFechaHoraAfter(fechaHora);
    }
    
    public List<EncuestaSatisfaccion> buscarEncuestasPorRangoSatisfaccion(int minScore, int maxScore) {
        return encuestaSatisfaccionRepository.findByQ6SatisfaccionBetween(minScore, maxScore);
    }

    public List<String> obtenerTextosQ8() {
        return obtenerTodasLasEncuestas().stream()
                .map(EncuestaSatisfaccion::getQ8Mejora)
                .filter(s -> s != null && !s.trim().isEmpty())
                .collect(Collectors.toList());
    }

    public List<String> obtenerTextosQ9() {
        return obtenerTodasLasEncuestas().stream()
                .map(EncuestaSatisfaccion::getQ9Comentarios)
                .filter(s -> s != null && !s.trim().isEmpty())
                .collect(Collectors.toList());
    }
}