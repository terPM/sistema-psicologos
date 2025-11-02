package mx.uam.ayd.proyecto.datos;

import mx.uam.ayd.proyecto.negocio.modelo.RegistroEmocional;
import org.springframework.data.repository.CrudRepository;
import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio para la entidad RegistroEmocional.
 */
public interface RegistroEmocionalRepository extends CrudRepository<RegistroEmocional, Long> {

    /**
     * Encuentra todos los registros de una fecha específica.
     *
     * @param fecha la fecha a buscar.
     * @return una lista de registros de esa fecha.
     */
    List<RegistroEmocional> findByFecha(LocalDate fecha);

}
