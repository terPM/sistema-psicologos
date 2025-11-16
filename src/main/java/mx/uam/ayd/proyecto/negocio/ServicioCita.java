package mx.uam.ayd.proyecto.negocio;

import mx.uam.ayd.proyecto.datos.CitaRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Cita;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;
import mx.uam.ayd.proyecto.negocio.modelo.TipoConfirmacionCita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
