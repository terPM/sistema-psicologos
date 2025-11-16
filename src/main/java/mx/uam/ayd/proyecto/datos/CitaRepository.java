package mx.uam.ayd.proyecto.datos;

import mx.uam.ayd.proyecto.negocio.modelo.Cita;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import mx.uam.ayd.proyecto.negocio.modelo.TipoConfirmacionCita;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.time.LocalDateTime;

public interface CitaRepository extends CrudRepository<Cita, Integer> {

    List<Cita> findByPaciente(Paciente paciente);

    List<Cita> findByEstadoCita(TipoConfirmacionCita estadoCita);

    Cita findByFechaCita(LocalDateTime fechaCita);

    List<Cita> findByPacienteAndEstadoCita(Paciente paciente, TipoConfirmacionCita estadoCita);

    /**
     * Busca la cita pendiente más próxima (la primera) para un paciente.
     *
     * @param paciente El paciente.
     * @param estadoCita El estado (ej. PENDIENTE).
     * @return La cita encontrada, o null si no hay.
     */
    Cita findTopByPacienteAndEstadoCitaOrderByFechaCitaAsc(Paciente paciente, TipoConfirmacionCita estadoCita);
}