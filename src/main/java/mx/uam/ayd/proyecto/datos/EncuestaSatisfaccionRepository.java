package mx.uam.ayd.proyecto.datos;

import mx.uam.ayd.proyecto.negocio.modelo.EncuestaSatisfaccion; 
import org.springframework.data.repository.CrudRepository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para gestionar operaciones de persistencia sobre la entidad {@link EncuestaSatisfaccion}.
 *
 * <p>Extiende {@link org.springframework.data.repository.CrudRepository CrudRepository},
 * heredando operaciones CRUD estándar para guardar y buscar las respuestas de la encuesta.</p>
 *
 * @author CODEX
 * @version 1.0
 */
public interface EncuestaSatisfaccionRepository extends CrudRepository<EncuestaSatisfaccion, Long> { 
    /**
     * Consulta por Fecha:
     * Encuentra todas las encuestas respondidas después de una fecha y hora específicas.
     *
     * @param fechaHora La marca de tiempo inicial (exclusiva) para buscar.
     * @return Una lista de encuestas realizadas a partir de ese momento.
     */
    List<EncuestaSatisfaccion> findByFechaHoraAfter(LocalDateTime fechaHora);

    /**
     * Consulta por Rango Numérico:
     * Encuentra todas las encuestas donde la calificación de Satisfacción (Q6)
     * está en el rango especificado.
     *
     * @param minScore Calificación mínima (ej. 1).
     * @param maxScore Calificación máxima (ej. 4).
     * @return Una lista de encuestas que cumplen con el rango de Q6.
     */
    List<EncuestaSatisfaccion> findByQ6SatisfaccionBetween(int minScore, int maxScore);

    /**
     * Consulta por Contenido de Texto:
     * Busca encuestas que contengan un fragmento de texto en el campo de Comentarios (Q9).
     *
     * @param comentario El texto a buscar. Usa 'Containing' para la operación LIKE de SQL.
     * @return Una lista de encuestas que contienen el texto.
     */
    List<EncuestaSatisfaccion> findByQ8MejoraContaining(String comentario);
    List<EncuestaSatisfaccion> findByQ9ComentariosContaining(String comentario);
    
}
