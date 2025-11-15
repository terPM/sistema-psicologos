package mx.uam.ayd.proyecto.negocio;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.PacienteRepository;
import mx.uam.ayd.proyecto.datos.CitaRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Cita;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;
import mx.uam.ayd.proyecto.negocio.modelo.TipoConfirmacionCita;

/**
 * Servicio relacionado con las citas agendadas entre pacientes y psicólogos.
 * <p>Proporciona métodos para crear nuevas citas, asegurando que no haya conflictos
 * de horario para los psicólogos.</p>
 * 
 * @author TechSolutions
 */
@Service
public class ServicioCita {
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private CitaRepository citaRepository;

    /*
     * Crea una nueva cita para un paciente logeado en una fecha y hora específicas.
     * @param paciente el paciente que agenda la cita; no debe ser {@code null}.
     * @param fechaCita la fecha y hora de la cita; no debe ser {@code null}.
     * @return la cita creada.
     * 
     */
    public Cita crearCita(String nombreUsuarioActivo, LocalDateTime fechaCita) {
        Paciente paciente = pacienteRepository.findByUsuario(nombreUsuarioActivo);
        Psicologo psicologo = paciente.getPsicologo();
        /*
         * Verifica si ya existe una cita para el psicólogo en la fecha y hora especificadas
         * y que no esté cancelada.
         * Si existe, lanza una excepción para evitar conflictos de horario.
         */
        Cita cita = citaRepository.findByPsicologoAndFechaCitaAndEstadoCitaNot(psicologo, fechaCita, TipoConfirmacionCita.CANCELADA);
        if(cita != null){
            throw new IllegalArgumentException("Ya existe una cita agendada en esta fecha y hora");
        }
        cita = new Cita();
        cita.setPaciente(paciente);
        cita.setPsicologo(psicologo);
        cita.setFechaCita(fechaCita);
        cita.setEstadoCita(TipoConfirmacionCita.PENDIENTE);
        
        citaRepository.save(cita);
        return cita;
    }


    /**
     * Obtiene la lista de próximas citas para el paciente identificado por su nombre de usuario.
     * 
     * @param nombreUsuarioActivo el nombre de usuario del paciente.
     * @return una lista de citas próximas del paciente.
     */
    public List<Cita> listarCitas(String nombreUsuarioActivo) {
        Paciente paciente = pacienteRepository.findByUsuario(nombreUsuarioActivo);
        List<Cita> citas = citaRepository.findByPacienteAndEstadoCitaNot(paciente, TipoConfirmacionCita.CANCELADA);
        return citas;
    }
    
    /**
     * Cancela una cita existente identificada por su ID.
     * 
     * @param idCita el ID de la cita a cancelar.
     * @return la cita cancelada.
     * @throws IllegalArgumentException si la cita no existe.
     */
    public Cita cancelarCita(int idCita) {
        Cita cita = citaRepository.findById(idCita).orElse(null);
        if (cita == null) {
            throw new IllegalArgumentException("La cita con id " + idCita + " no existe.");
        }
        cita.setEstadoCita(TipoConfirmacionCita.CANCELADA);
        //cita.setMotivoCancelacion(motivoCancelacion);
        citaRepository.save(cita);
        return cita;
    }
    
}
