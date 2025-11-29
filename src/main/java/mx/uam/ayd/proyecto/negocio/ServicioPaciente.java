package mx.uam.ayd.proyecto.negocio;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import mx.uam.ayd.proyecto.datos.PacienteRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;

/**
 * Servicio que gestiona la lógica de negocio relacionada con los pacientes.
 *
 * @author Tech Solutions
 * @version 1.0
 */
@Service
public class ServicioPaciente {

    private static final Logger log = LoggerFactory.getLogger(ServicioPaciente.class);

    @Autowired
    private PacienteRepository pacienteRepository;

    /**
     * Registra un nuevo paciente en el sistema.
     */
    public Paciente agregarPaciente(String nombre, String correo, String telefono, int edad, String usuario, String contrasena) {
        if(nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacio");
        }
        if(correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo no puede ser nulo o vacio");
        }
        if(telefono == null || telefono.trim().isEmpty()) {
            throw new IllegalArgumentException("El telefono no puede ser nulo o vacio");
        }
        if(usuario == null || usuario.trim().isEmpty()){
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        if (contrasena==null || contrasena.trim().isEmpty()){
            throw new IllegalArgumentException("La contraseña no puede ser nula");
        }

        Paciente paciente = pacienteRepository.findByCorreo(correo);
        if (paciente != null){
            throw new IllegalArgumentException("Este correo ya ha sido registrado en el centro");
        }
        paciente = pacienteRepository.findByUsuario(usuario);
        if (paciente != null){
            throw new IllegalArgumentException("Ya existe un paciente con este usuario");
        }
        log.info("Agregando paciente nombre: "+nombre+" correo: "+correo);

        paciente = new Paciente();
        paciente.setNombre(nombre);
        paciente.setCorreo(correo);
        paciente.setTelefono(telefono);
        paciente.setEdad(edad);
        paciente.setUsuario(usuario);
        paciente.setContrasena(contrasena);

        pacienteRepository.save(paciente);

        return paciente;
    }

    /**
     * Recupera todos los pacientes registrados en el sistema.
     */
    public List<Paciente> recuperarTodosLosPacientes() {
        List<Paciente> pacientes = new ArrayList<>();
        pacienteRepository.findAll().forEach(pacientes::add);
        return pacientes;
    }

    /**
     * Recupera solo los pacientes asignados a un psicólogo específico.
     */
    public List<Paciente> recuperarPacientesPorPsicologo(Psicologo psicologo) {
        return pacienteRepository.findByPsicologo(psicologo);
    }

    /**
     * Asigna un psicólogo a un paciente existente.
     */
    @Transactional
    public void asignarPsicologo(Paciente paciente, Psicologo psicologo) {
        paciente.setPsicologo(psicologo);
        pacienteRepository.save(paciente);
    }

    public List<Paciente> obtenerPacientes() {
        return recuperarTodosLosPacientes();
    }

    /**
     * Obtiene un paciente por su nombre de usuario.
     */
    public Paciente obtenerPacientePorUsuario(String usuario) {
        return pacienteRepository.findByUsuario(usuario);
    }

    /**
     * Actualiza solo la contraseña del paciente.
     */
    public void actualizarContrasena(Paciente paciente, String nuevaContrasena) {
        paciente.setContrasena(nuevaContrasena);
        pacienteRepository.save(paciente);
    }

    /**
     * Guarda cualquier cambio hecho al objeto paciente (Nombre, Edad, Teléfono, etc.).
     * Este método es esencial para el módulo de actualización de información.
     */
    @Transactional
    public void actualizarPaciente(Paciente paciente) {
        pacienteRepository.save(paciente);
    }
}