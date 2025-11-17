package mx.uam.ayd.proyecto.datos;

import mx.uam.ayd.proyecto.negocio.modelo.Cita;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;
import mx.uam.ayd.proyecto.negocio.modelo.TipoConfirmacionCita;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

public interface CitaRepository extends CrudRepository<Cita, Integer> {

    List<Cita> findByPaciente(Paciente paciente);
<<<<<<< HEAD

    /**
     * Recupera todas las citas asociadas a un paciente.
     *
     * @param paciente el paciente del cual se quieren obtener las citas; no debe ser {@code null}.
     * @return una lista de citas del paciente; si no tiene citas registradas, la lista estará vacía.
     */
    List<Cita> findByPacienteAndEstadoCitaNot(Paciente paciente, TipoConfirmacionCita estadoCita);

    /**
     * Busca una cita por su identificador único.
     *
     * @param id el identificador de la cita.
     * @return un {@link Optional} que contiene la cita si se encuentra; {@code Optional.empty()} si no existe.
     */
    Optional<Cita> findById(int id);
=======
>>>>>>> hu-16-historial-de-pagos

    List<Cita> findByEstadoCita(TipoConfirmacionCita estadoCita);

    Cita findByFechaCita(LocalDateTime fechaCita);

    List<Cita> findByPacienteAndEstadoCita(Paciente paciente, TipoConfirmacionCita estadoCita);

    /**
<<<<<<< HEAD
     * Busca una cita por psicólogo y fecha específica, excluyendo aquellas cuyo estado no sea cancelado.
     *
     * @param psicologo el psicólogo asociado a la cita; no debe ser {@code null}.
     * @param fechaCita la fecha y hora de la cita; no debe ser {@code null}.
     * @return la cita correspondiente al psicólogo en la fecha especificada; si no existe, retorna {@code null}.
     */
    Cita findByPsicologoAndFechaCitaAndEstadoCitaNot(Psicologo psicologo, LocalDateTime fechaCita, TipoConfirmacionCita estadoCita);

    /**
     * Busca citas por paciente y estado.
     * Recupera todas las citas de un psicólogo, trayendo (FETCH)
     * la información del Paciente asociado en la misma consulta
     * para evitar LazyInitializationException.
     *
     * @param psicologo El psicólogo del cual se desean obtener las citas.
     * @return Una lista de citas con los datos del paciente ya cargados.
     */
    @Query("SELECT c FROM Cita c LEFT JOIN FETCH c.paciente WHERE c.psicologo = :psicologo")
    List<Cita> findByPsicologo(@Param("psicologo") Psicologo psicologo);

    @Query("SELECT c FROM Cita c " +
            "LEFT JOIN FETCH c.paciente " +
            "LEFT JOIN FETCH c.psicologo " +
            "WHERE c.id = :id")
    Cita findByIdConRelaciones(@Param("id") int id);
}
=======
     * Busca la cita pendiente más próxima (la primera) para un paciente.
     *
     * @param paciente El paciente.
     * @param estadoCita El estado (ej. PENDIENTE).
     * @return La cita encontrada, o null si no hay.
     */
    Cita findTopByPacienteAndEstadoCitaOrderByFechaCitaAsc(Paciente paciente, TipoConfirmacionCita estadoCita);
}
>>>>>>> hu-16-historial-de-pagos
