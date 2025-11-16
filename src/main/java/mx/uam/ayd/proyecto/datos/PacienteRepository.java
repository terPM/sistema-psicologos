package mx.uam.ayd.proyecto.datos;

import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends CrudRepository<Paciente, Long> {

    Paciente findByCorreo(String correo);

    List<Paciente> findByEdadBetween(int edad1, int edad2);

    Paciente findByUsuario(String usuario);

    /**
     * Encuentra todos los pacientes asignados a un psicólogo específico.
     *
     * @param psicologo El psicólogo.
     * @return Una lista de sus pacientes.
     */
    List<Paciente> findByPsicologo(Psicologo psicologo);
}