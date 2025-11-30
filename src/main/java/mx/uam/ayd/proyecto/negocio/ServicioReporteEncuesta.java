package mx.uam.ayd.proyecto.negocio;

import mx.uam.ayd.proyecto.datos.EncuestaSatisfaccionRepository;
import mx.uam.ayd.proyecto.negocio.modelo.EncuestaSatisfaccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Servicio de Negocio dedicado a la obtención de los datos
 * estadísticos de las Encuestas de Satisfacción para su presentación en el reporte.
 */
@Service 
public class ServicioReporteEncuesta { 

    @Autowired
    private EncuestaSatisfaccionRepository encuestaSatisfaccionRepository;

    /**
     * Obtiene todas las encuestas registradas.
     */
    private List<EncuestaSatisfaccion> obtenerTodasLasEncuestas() {
        return StreamSupport.stream(encuestaSatisfaccionRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * Calcula el conteo de respuestas (1-4) para una pregunta específica de opción múltiple.
     * @param preguntaId El identificador de la pregunta (e.g., "Pregunta 3").
     * @return Un mapa donde la clave es la calificación (1-4) y el valor es la frecuencia.
     */
    public Map<Integer, Long> obtenerConteoRespuestas(String preguntaId) {
        
        List<EncuestaSatisfaccion> encuestas = obtenerTodasLasEncuestas();
        ToIntFunction<EncuestaSatisfaccion> getter;
        
        switch (preguntaId) {
            case "Pregunta 1": getter = EncuestaSatisfaccion::getQ1Empatia; break;
            case "Pregunta 2": getter = EncuestaSatisfaccion::getQ2Confianza; break;
            case "Pregunta 3": getter = EncuestaSatisfaccion::getQ3Respeto; break;
            case "Pregunta 4": getter = EncuestaSatisfaccion::getQ4Confidencialidad; break;
            case "Pregunta 5": getter = EncuestaSatisfaccion::getQ5Herramientas; break;
            case "Pregunta 6": getter = EncuestaSatisfaccion::getQ6Satisfaccion; break;
            case "Pregunta 7": getter = EncuestaSatisfaccion::getQ7Recomendacion; break;
            default: return Map.of();
        }
        return encuestas.stream()
                .filter(e -> getter.applyAsInt(e) > 0) 
                .collect(Collectors.groupingBy(
                    getter::applyAsInt, 
                    Collectors.counting()
                ));
    }
    
    /**
     * Obtiene una lista de los textos de sugerencias (Q8) o comentarios (Q9).
     * @param preguntaId El identificador de la pregunta ("Pregunta 8" o "Pregunta 9").
     * @return Una lista de Strings con el contenido de las respuestas abiertas.
     */
    public List<String> obtenerRespuestasAbiertas(String preguntaId) {
        
        List<EncuestaSatisfaccion> encuestas = obtenerTodasLasEncuestas();
        
        if (preguntaId.equals("Pregunta 8")) {
            return encuestas.stream()
                    .map(EncuestaSatisfaccion::getQ8Mejora)
                    .filter(s -> s != null && !s.trim().isEmpty())
                    .collect(Collectors.toList());
        } else if (preguntaId.equals("Pregunta 9")) {
            return encuestas.stream()
                    .map(EncuestaSatisfaccion::getQ9Comentarios)
                    .filter(s -> s != null && !s.trim().isEmpty())
                    .collect(Collectors.toList());
        }
        return List.of("Pregunta no reconocida.");
    }
}
