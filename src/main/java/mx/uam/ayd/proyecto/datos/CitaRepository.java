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

    /**
     * Recupera todas las citas asociadas a un paciente que NO estén canceladas.
     * (de HEAD)
     */
    List<Cita> findByPacienteAndEstadoCitaNot(Paciente paciente, TipoConfirmacionCita estadoCita);

    /**
     * Busca una cita por su identificador único.
     * (de HEAD)
     */
    Optional<Cita> findById(int id);

    List<Cita> findByEstadoCita(TipoConfirmacionCita estadoCita);

    Cita findByFechaCita(LocalDateTime fechaCita);

    List<Cita> findByPacienteAndEstadoCita(Paciente paciente, TipoConfirmacionCita estadoCita);

    /**
     * Busca una cita por psicólogo y fecha específica, excluyendo canceladas.
     * (de HEAD)
     */
    Cita findByPsicologoAndFechaCitaAndEstadoCitaNot(Psicologo psicologo, LocalDateTime fechaCita, TipoConfirmacionCita estadoCita);

    /**
     * Busca citas por psicólogo y trae al paciente (para evitar LazyInitializationException).
     * (de HEAD)
     */
    @Query("SELECT c FROM Cita c LEFT JOIN FETCH c.paciente WHERE c.psicologo = :psicologo")
    List<Cita> findByPsicologo(@Param("psicologo") Psicologo psicologo);

    @Query("SELECT c FROM Cita c " +
            "LEFT JOIN FETCH c.paciente " +
            "LEFT JOIN FETCH c.psicologo " +
            "WHERE c.id = :id")
    Cita findByIdConRelaciones(@Param("id") int id);

    /**
     * Busca la cita pendiente más próxima (la primera) para un paciente.
     * (de hu-16-historial-de-pagos)
     */
    Cita findTopByPacienteAndEstadoCitaOrderByFechaCitaAsc(Paciente paciente, TipoConfirmacionCita estadoCita);

    /**
     * HU-03: Busca todas las citas de un paciente que sean posteriores a una fecha dada
     * (usualmente "ahora") y que NO estén en un estado específico (ej. CANCELADA).
     */
    List<Cita> findByPacienteAndFechaCitaAfterAndEstadoCitaNot(Paciente paciente, LocalDateTime fecha, TipoConfirmacionCita estadoCita);
}