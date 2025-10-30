package mx.uam.ayd.proyecto.datos;

import mx.uam.ayd.proyecto.negocio.modelo.Aviso;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.time.LocalDate;

/**
 * Repositorio para gestionar operaciones de persistencia sobre la entidad {@link Aviso}.
 *
 * <p>Proporciona métodos de búsqueda personalizados, como encontrar el último aviso
 * o buscar por fecha.
 * Extiende {@link org.springframework.data.repository.CrudRepository CrudRepository},
 * heredando operaciones CRUD estándar.</p>
 *
 * @author Tech Solutions (adaptado de PsicologoRepository)
 * @version 1.0
 */
public interface AvisoRepository extends CrudRepository<Aviso, Integer> {

/**
     * Encuentra el aviso más reciente ordenando por ID de forma descendente.
     * Como el ID es auto-incremental, esto garantiza que obtenemos
     * el último aviso que fue insertado.
     *
     * @return el último {@link Aviso} publicado o {@code null} si no existe ninguno.
     */
    Aviso findTopByOrderByIdDesc();

    /**
     * Encuentra todos los avisos publicados en una fecha específica.
     *
     * @param fecha la fecha a buscar; no debe ser {@code null}.
     * @return una lista de avisos publicados en esa fecha; si no hay coincidencias, 
     * la lista estará vacía.
     */
    List<Aviso> findByFecha(LocalDate fecha);
}