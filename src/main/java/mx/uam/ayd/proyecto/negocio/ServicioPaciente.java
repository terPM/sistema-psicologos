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

@Service
public class ServicioPaciente {

    private static final Logger log = LoggerFactory.getLogger(ServicioPaciente.class);

    @Autowired
    private PacienteRepository pacienteRepository;

    public Paciente agregarPaciente(String nombre, String correo, String telefono, int edad, String usuario, String contrasena) {
        if(nombre == null || nombre.trim().isEmpty()) throw new IllegalArgumentException("El nombre no puede ser nulo o vacio");
        // ... (resto de validaciones) ...

        Paciente paciente = pacienteRepository.findByCorreo(correo);
        if (paciente != null) throw new IllegalArgumentException("Este correo ya ha sido registrado");

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

    public List<Paciente> recuperarTodosLosPacientes() {
        List<Paciente> pacientes = new ArrayList<>();
        pacienteRepository.findAll().forEach(pacientes::add);
        return pacientes;
    }

    public List<Paciente> recuperarPacientesPorPsicologo(Psicologo psicologo) {
        return pacienteRepository.findByPsicologo(psicologo);
    }

    @Transactional
    public void asignarPsicologo(Paciente paciente, Psicologo psicologo) {
        paciente.setPsicologo(psicologo);
        pacienteRepository.save(paciente);
    }

    public List<Paciente> obtenerPacientes() {
        return recuperarTodosLosPacientes();
    }

    public Paciente obtenerPacientePorUsuario(String usuario) {
        return pacienteRepository.findByUsuario(usuario);
    }

    public void actualizarContrasena(Paciente paciente, String nuevaContrasena) {
        paciente.setContrasena(nuevaContrasena);
        pacienteRepository.save(paciente);
    }

    /**
     * Método para guardar cualquier cambio en el paciente.
     * Usamos @Transactional para asegurar que se comprometa en la BD.
     */
    @Transactional
    public void actualizarPaciente(Paciente paciente) {
        pacienteRepository.save(paciente);
    }
}