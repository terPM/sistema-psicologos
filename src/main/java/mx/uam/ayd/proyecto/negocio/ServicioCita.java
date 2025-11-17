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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
@Service
public class ServicioCita {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private ServicioNotificacion servicioNotificacion;

    public Cita crearCita(Paciente paciente, LocalDateTime fechaCita, String motivo) {
        Psicologo psicologo = paciente.getPsicologo();

        Cita existente = citaRepository.findByFechaCita(fechaCita);
        if (existente != null) {
            throw new IllegalArgumentException("Ya existe una cita agendada en esta fecha y hora");
        }

        Cita cita = new Cita();
        cita.setPaciente(paciente);
        cita.setPsicologo(psicologo);
        cita.setFechaCita(fechaCita);
        cita.setEstadoCita(TipoConfirmacionCita.PENDIENTE);
        
        cita.setMotivo(motivo);

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
    /*
     * Recupera todas las citas asociadas a un psicólogo específico.
     *
     * @param psicologo El psicólogo del cual se desean obtener las citas.
     * @return Una lista de citas; vacía si no se encuentran.
     * @throws IllegalArgumentException si el psicólogo es nulo.
     */
    public List<Cita> obtenerCitasPorPsicologo(Psicologo psicologo) {
        if (psicologo == null) {
            throw new IllegalArgumentException("El psicólogo no puede ser nulo");
        }
        return citaRepository.findByPsicologo(psicologo);
    }
    
    public void reagendarCita(int idCita, LocalDate nuevaFecha, String nuevaHora, Psicologo nuevoPsicologo) {
        // Cargar con relaciones para evitar LazyInitializationException
        Cita cita = citaRepository.findByIdConRelaciones(idCita);
        if (cita == null) {
            throw new IllegalArgumentException("Cita no encontrada: " + idCita);
        }

        // convertir “HH:mm” a LocalDateTime
        int hora = Integer.parseInt(nuevaHora.split(":")[0]);
        LocalDateTime nuevaFechaHora = nuevaFecha.atTime(hora, 0);

        // aplicar cambios
        cita.setFechaCita(nuevaFechaHora);
        cita.setPsicologo(nuevoPsicologo);
        citaRepository.save(cita);

        // notificación al nuevo psicólogo
        String nombrePaciente = (cita.getPaciente() != null)
                ? cita.getPaciente().getNombre()
                : "el paciente";

        String mensaje = String.format("La cita con %s fue reagendada a %s",
                nombrePaciente, nuevaFechaHora.toString());

        servicioNotificacion.crearNotificacion(nuevoPsicologo, mensaje);
    }
}
