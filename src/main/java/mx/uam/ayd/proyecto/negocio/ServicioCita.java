package mx.uam.ayd.proyecto.negocio;

import java.time.LocalDate;
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

@Service
public class ServicioCita {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private ServicioNotificacion servicioNotificacion;

    /**
     * Crea una cita nueva.
     */
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
     * Lista todas las citas activas del paciente.
     */
    public List<Cita> listarCitas(String nombreUsuarioActivo) {
        Paciente paciente = pacienteRepository.findByUsuario(nombreUsuarioActivo);
        return citaRepository.findByPacienteAndEstadoCitaNot(paciente, TipoConfirmacionCita.CANCELADA);
    }

    /**
     * Cancela una cita.
     */
    public Cita cancelarCita(int idCita) {
        Cita cita = citaRepository.findById(idCita).orElse(null);
        if (cita == null) {
            throw new IllegalArgumentException("La cita con id " + idCita + " no existe.");
        }

        cita.setEstadoCita(TipoConfirmacionCita.CANCELADA);
        citaRepository.save(cita);
        return cita;
    }

    /**
     * Obtiene citas por psicólogo.
     */
    public List<Cita> obtenerCitasPorPsicologo(Psicologo psicologo) {
        if (psicologo == null) {
            throw new IllegalArgumentException("El psicólogo no puede ser nulo");
        }
        return citaRepository.findByPsicologo(psicologo);
    }

    /**
     * Reagenda una cita y notifica al psicólogo.
     */
    public void reagendarCita(int idCita, LocalDate nuevaFecha, String nuevaHora, Psicologo nuevoPsicologo) {

        Cita cita = citaRepository.findByIdConRelaciones(idCita);
        if (cita == null) {
            throw new IllegalArgumentException("Cita no encontrada: " + idCita);
        }

        int hora = Integer.parseInt(nuevaHora.split(":")[0]);
        LocalDateTime nuevaFechaHora = nuevaFecha.atTime(hora, 0);

        cita.setFechaCita(nuevaFechaHora);
        cita.setPsicologo(nuevoPsicologo);
        citaRepository.save(cita);

        String nombrePaciente = (cita.getPaciente() != null)
                ? cita.getPaciente().getNombre()
                : "el paciente";

        String mensaje = String.format("La cita con %s fue reagendada a %s",
                nombrePaciente, nuevaFechaHora.toString());

        servicioNotificacion.crearNotificacion(nuevoPsicologo, mensaje);
    }
}
