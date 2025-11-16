package mx.uam.ayd.proyecto.negocio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.PacienteRepository;
import mx.uam.ayd.proyecto.datos.RegistroEmocionalRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;
import mx.uam.ayd.proyecto.negocio.modelo.RegistroEmocional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ServicioRegistroemocional {

    private static final Logger log = LoggerFactory.getLogger(ServicioRegistroemocional.class);

    @Autowired
    private RegistroEmocionalRepository registroEmocionalRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    /**
     * Guarda un nuevo registro emocional VINCULADO a un paciente.
     */
    public RegistroEmocional guardarRegistro(String emocion, String nota, Paciente paciente) {
        if (emocion == null || emocion.trim().isEmpty()) {
            throw new IllegalArgumentException("La emoción no puede estar vacía");
        }

        if (paciente == null) {
            throw new IllegalArgumentException("El paciente no puede ser nulo");
        }

        LocalDate fechaHoy = LocalDate.now();
        boolean yaExisteRegistroHoy = registroEmocionalRepository.existsByPacienteAndFecha(paciente, fechaHoy);

        if (yaExisteRegistroHoy) {
            throw new IllegalArgumentException("Ya se realizó un registro emocional para este paciente el día de hoy.");
        }

        log.info("Guardando nuevo registro emocional para el paciente: " + paciente.getNombre());

        RegistroEmocional registro = new RegistroEmocional();
        registro.setFecha(fechaHoy);
        registro.setEmocion(emocion);
        registro.setNota(nota);
        registro.setPaciente(paciente);

        registroEmocionalRepository.save(registro);
        return registro;
    }

    /**
     * Obtiene TODOS los registros emocionales (para el Admin).
     */
    public List<RegistroEmocional> listarRegistros() {
        List<RegistroEmocional> registros = new ArrayList<>();
        registroEmocionalRepository.findAll().forEach(registros::add);
        return registros;
    }

    /**
     * Obtiene solo los registros de un paciente específico.
     */
    public List<RegistroEmocional> listarRegistrosPorPaciente(Paciente paciente) {
        return registroEmocionalRepository.findByPaciente(paciente);
    }

    /**
     * Obtiene los registros emocionales de todos los pacientes
     * asignados a un psicólogo específico.
     */
    public List<RegistroEmocional> listarRegistrosPorPsicologo(Psicologo psicologo) {
        if (psicologo == null) {
            return Collections.emptyList();
        }

        List<Paciente> pacientesDelPsicologo = pacienteRepository.findByPsicologo(psicologo);

        if (pacientesDelPsicologo == null || pacientesDelPsicologo.isEmpty()) {
            return Collections.emptyList();
        }

        return registroEmocionalRepository.findByPacienteIn(pacientesDelPsicologo);
    }
}