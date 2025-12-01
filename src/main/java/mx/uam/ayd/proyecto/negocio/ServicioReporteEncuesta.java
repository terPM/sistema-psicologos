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

@Service 
public class ServicioReporteEncuesta { 

    @Autowired
    private EncuestaSatisfaccionRepository encuestaSatisfaccionRepository;

    // 1. Mapa estático para eliminar el bloque switch (de la refactorización anterior)
    private static final Map<String, ToIntFunction<EncuestaSatisfaccion>> GETTER_MAP = Map.of(
        "Pregunta 1", EncuestaSatisfaccion::getQ1Empatia,
        "Pregunta 2", EncuestaSatisfaccion::getQ2Confianza,
        "Pregunta 3", EncuestaSatisfaccion::getQ3Respeto,
        "Pregunta 4", EncuestaSatisfaccion::getQ4Confidencialidad,
        "Pregunta 5", EncuestaSatisfaccion::getQ5Herramientas,
        "Pregunta 6", EncuestaSatisfaccion::getQ6Satisfaccion,
        "Pregunta 7", EncuestaSatisfaccion::getQ7Recomendacion
    );
    
    // 2. NUEVO: Mapa de traducción de número (clave) a etiqueta (valor)
    private static final Map<Integer, String> ETIQUETA_MAP = Map.of(
        1, "Malo",
        2, "Regular",
        3, "Bueno",
        4, "Excelente"
    );

    /**
     * Obtiene todas las encuestas registradas.
     */
    private List<EncuestaSatisfaccion> obtenerTodasLasEncuestas() {
        return StreamSupport.stream(encuestaSatisfaccionRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * Calcula el conteo de respuestas para una pregunta específica,
     * devolviendo el resultado mapeado a etiquetas textuales.
     * @param preguntaId El identificador de la pregunta (e.g., "Pregunta 3").
     * @return Un mapa donde la clave es la etiqueta (String: Malo, Regular, etc.) y el valor es la frecuencia (Long).
     */
    public Map<String, Long> obtenerConteoRespuestas(String preguntaId) { // CAMBIAMOS Map<Integer, Long> a Map<String, Long>
        
        List<EncuestaSatisfaccion> encuestas = obtenerTodasLasEncuestas();
        ToIntFunction<EncuestaSatisfaccion> getter = GETTER_MAP.get(preguntaId);
        
        if (getter == null) {
            return Map.of();
        }

        // Paso 1: Agrupamos y contamos por el número (1, 2, 3, 4)
        Map<Integer, Long> conteoNumerico = encuestas.stream()
            .filter(e -> getter.applyAsInt(e) > 0) 
            .collect(Collectors.groupingBy(
                getter::applyAsInt, 
                Collectors.counting()
            ));

        // Paso 2: Transformamos las claves numéricas a etiquetas textuales (String)
        return conteoNumerico.entrySet().stream()
            .collect(Collectors.toMap(
                entry -> ETIQUETA_MAP.getOrDefault(entry.getKey(), "Desconocido"), // Transforma 1 -> Malo, 4 -> Excelente
                Map.Entry::getValue // Mantiene el conteo
            ));
    }
    
    /**
     * Obtiene una lista de los textos de sugerencias (Q8) o comentarios (Q9).
     * @param preguntaId El identificador de la pregunta ("Pregunta 8" o "Pregunta 9").
     * @return Una lista de Strings con el contenido de las respuestas abiertas.
     */
    public List<String> obtenerRespuestasAbiertas(String preguntaId) {
        
        List<EncuestaSatisfaccion> encuestas = obtenerTodasLasEncuestas();
        
        if ("Pregunta 8".equals(preguntaId)) {
            return encuestas.stream()
                    .map(EncuestaSatisfaccion::getQ8Mejora)
                    .filter(s -> s != null && !s.trim().isEmpty())
                    .collect(Collectors.toList());
        } else if ("Pregunta 9".equals(preguntaId)) {
            return encuestas.stream()
                    .map(EncuestaSatisfaccion::getQ9Comentarios)
                    .filter(s -> s != null && !s.trim().isEmpty())
                    .collect(Collectors.toList());
        }
        return List.of(); 
    }
}