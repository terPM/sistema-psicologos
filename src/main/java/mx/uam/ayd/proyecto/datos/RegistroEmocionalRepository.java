package mx.uam.ayd.proyecto.datos;

import mx.uam.ayd.proyecto.negocio.modelo.RegistroEmocional;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import org.springframework.data.repository.CrudRepository;
import java.time.LocalDate;
import java.util.List;

public interface RegistroEmocionalRepository extends CrudRepository<RegistroEmocional, Long> {

    List<RegistroEmocional> findByFecha(LocalDate fecha);

    List<RegistroEmocional> findByPaciente(Paciente paciente);

    List<RegistroEmocional> findByPacienteIn(List<Paciente> pacientes);

    /**
     * Verifica si ya existe un registro para un paciente específico en una fecha específica.
     *
     * @param paciente El paciente a verificar.
     * @param fecha La fecha a verificar.
     * @return true si ya existe un registro, false en caso contrario.
     */
    boolean existsByPacienteAndFecha(Paciente paciente, LocalDate fecha);
}