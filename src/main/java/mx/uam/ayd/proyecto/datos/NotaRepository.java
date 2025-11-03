package mx.uam.ayd.proyecto.datos;

import mx.uam.ayd.proyecto.negocio.modelo.Nota;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import org.springframework.data.repository.CrudRepository;
import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio para manejar operaciones CRUD de la entidad Nota.
 */
public interface NotaRepository extends CrudRepository<Nota, Long> {

    /**
     * Encuentra todas las notas asociadas a un paciente específico.
     *
     * @param paciente el paciente al que pertenecen las notas.
     * @return lista de notas encontradas.
     */
    List<Nota> findByPaciente(Paciente paciente);

    /**
     * Encuentra las notas registradas en una fecha específica.
     *
     * @param fecha la fecha de la nota.
     * @return lista de notas encontradas.
     */
    List<Nota> findByFecha(LocalDate fecha);
}
