package mx.uam.ayd.proyecto.negocio;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.uam.ayd.proyecto.datos.PacienteRepository;
import mx.uam.ayd.proyecto.datos.CitaRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Cita;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;
import mx.uam.ayd.proyecto.negocio.modelo.TipoConfirmacionCita;

// Imports de ambas ramas
import mx.uam.ayd.proyecto.negocio.ServicioNotificacion;
import mx.uam.ayd.proyecto.negocio.ServicioLineaCaptura;

@Service
public class ServicioCita {

    @Autowired
    private PacienteRepository pacienteRepository; // de HEAD

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private ServicioNotificacion servicioNotificacion; // de HEAD

    @Autowired
    private ServicioLineaCaptura servicioLineaCaptura; // de hu-16

    /**
     * Crea una cita nueva y le adjunta la info de pago.
     * (Lógica de 'crearCita' de hu-16 fusionada con la de HEAD)
     */
    @Transactional
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

        // Lógica de hu-16 (pago)
        cita.setLineaCaptura(servicioLineaCaptura.generarLineaCaptura());
        cita.setMonto(servicioLineaCaptura.asignarPrecioCita());
        cita.setFechaVencimiento(fechaCita.toLocalDate());

        citaRepository.save(cita);
        return cita;
    }

    // --- MÉTODOS DE LA RAMA HEAD ---

    /**
     * Lista todas las citas activas del paciente.
     */
    @Transactional
    public List<Cita> listarCitas(String nombreUsuarioActivo) {
        Paciente paciente = pacienteRepository.findByUsuario(nombreUsuarioActivo);
        return citaRepository.findByPacienteAndEstadoCitaNot(paciente, TipoConfirmacionCita.CANCELADA);
    }

    /**
     * Cancela una cita.
     */
    @Transactional
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
    @Transactional
    public List<Cita> obtenerCitasPorPsicologo(Psicologo psicologo) {
        if (psicologo == null) {
            throw new IllegalArgumentException("El psicólogo no puede ser nulo");
        }
        return citaRepository.findByPsicologo(psicologo);
    }

    /**
     * Reagenda una cita y notifica al psicólogo.
     */
    @Transactional
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

    // --- MÉTODOS DE LA RAMA hu-16-historial-de-pagos ---

    @Transactional
    public Cita buscarCitaPendienteMasReciente(Paciente paciente) {
        Cita cita = citaRepository.findTopByPacienteAndEstadoCitaOrderByFechaCitaAsc(paciente, TipoConfirmacionCita.PENDIENTE);

        // Arregla LazyInitializationException
        if (cita != null) {
            if (cita.getPaciente() != null) {
                cita.getPaciente().getNombre();
            }
            if (cita.getPsicologo() != null) {
                cita.getPsicologo().getNombre();
            }
        }

        return cita;
    }

    @Transactional
    public List<Cita> listarCitasPorPaciente(Paciente paciente) {
        List<Cita> citas = citaRepository.findByPaciente(paciente);

        // Arregla LazyInitializationException
        for (Cita cita : citas) {
            if (cita.getPaciente() != null) cita.getPaciente().getNombre();
            if (cita.getPsicologo() != null) cita.getPsicologo().getNombre();
        }
        return citas;
    }

    /**
     * Obtiene una cita por su ID.
     */
    public Cita obtenerCitaPorId(int id) {
    return citaRepository.findById(id).orElse(null);
    }
}