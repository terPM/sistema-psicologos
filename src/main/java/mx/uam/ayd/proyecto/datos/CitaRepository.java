package mx.uam.ayd.proyecto.datos;

import mx.uam.ayd.proyecto.negocio.modelo.Cita;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import mx.uam.ayd.proyecto.negocio.modelo.TipoConfirmacionCita;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CitaRepository extends CrudRepository<Cita, Integer> {

    List<Cita> findByPaciente(Paciente paciente);

    List<Cita> findByEstadoCita(TipoConfirmacionCita estadoCita);

    Cita findByFechaCita(LocalDateTime fechaCita);

    List<Cita> findByPacienteAndEstadoCita(Paciente paciente, TipoConfirmacionCita estadoCita);

    @Query("SELECT c FROM Cita c " +
            "LEFT JOIN FETCH c.paciente " +
            "LEFT JOIN FETCH c.psicologo " +
            "WHERE c.id = :id")
    Cita findByIdConRelaciones(@Param("id") int id);
}
