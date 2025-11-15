package mx.uam.ayd.proyecto.datos;

import mx.uam.ayd.proyecto.negocio.modelo.Cita;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;
import mx.uam.ayd.proyecto.negocio.modelo.TipoConfirmacionCita;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repositorio para gestionar operaciones de persistencia sobre la entidad {@link Cita}.
 *
 * <p>Proporciona métodos para consultar citas asociadas a pacientes,
 * filtrar por estado de confirmación o por fecha, y realizar combinaciones de criterios.</p>
 *
 * <p>Extiende {@link org.springframework.data.repository.CrudRepository CrudRepository},
 * heredando las operaciones CRUD básicas.</p>
 *
 * @author Tech Solutions
 * @version 1.0
 */
public interface CitaRepository extends CrudRepository<Cita, Integer> {

    /**
     * Recupera todas las citas asociadas a un paciente.
     *
     * @param paciente el paciente del cual se quieren obtener las citas; no debe ser {@code null}.
     * @return una lista de citas del paciente; si no tiene citas registradas, la lista estará vacía.
     */
    List<Cita> findByPaciente(Paciente paciente);

    /**
     * Busca una cita por su identificador único.
     *
     * @param id el identificador de la cita.
     * @return un {@link Optional} que contiene la cita si se encuentra; {@code Optional.empty()} si no existe.
     */
    Optional<Cita> findById(int id);

    /**
     * Busca citas por estado.
     *
     * @param estadoCita el estado de la cita; no debe ser {@code null}.
     * @return una lista de citas con el estado especificado; si no hay coincidencias, la lista estará vacía.
     */
    List<Cita> findByEstadoCita(TipoConfirmacionCita estadoCita);

    /**
     * Busca una cita por psicólogo y fecha específica, excluyendo aquellas cuyo estado no sea cancelado.
     *
     * @param psicologo el psicólogo asociado a la cita; no debe ser {@code null}.
     * @param fechaCita la fecha y hora de la cita; no debe ser {@code null}.
     * @return la cita correspondiente al psicólogo en la fecha especificada; si no existe, retorna {@code null}.
     */
    Cita findByPsicologoAndFechaCitaAndEstadoCitaNot(Psicologo psicologo, LocalDateTime fechaCita, TipoConfirmacionCita estadoCita);

    /**
     * Busca citas por paciente y estado.
     *
     * @param paciente el paciente; no debe ser {@code null}.
     * @param estadoCita el estado de la cita; no debe ser {@code null}.
     * @return una lista de citas del paciente con el estado especificado; si no hay coincidencias, la lista estará vacía.
     */
    List<Cita> findByPacienteAndEstadoCita(Paciente paciente, TipoConfirmacionCita estadoCita);
}