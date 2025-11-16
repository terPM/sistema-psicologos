package mx.uam.ayd.proyecto.negocio;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.uam.ayd.proyecto.datos.CitaRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Cita;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;
import mx.uam.ayd.proyecto.negocio.modelo.TipoConfirmacionCita;

@Service
public class ServicioCita {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private ServicioLineaCaptura servicioLineaCaptura;

    public Cita crearCita(Paciente paciente, LocalDateTime fechaCita, String motivo) {
        Psicologo psicologo = paciente.getPsicologo();

        Cita cita = citaRepository.findByFechaCita(fechaCita);
        if(cita != null){
            throw new IllegalArgumentException("Ya existe una cita agendada en esta fecha y hora");
        }
        cita = new Cita();
        cita.setPaciente(paciente);
        cita.setPsicologo(psicologo);
        cita.setFechaCita(fechaCita);
        cita.setEstadoCita(TipoConfirmacionCita.PENDIENTE);
        cita.setMotivo(motivo);

        cita.setLineaCaptura(servicioLineaCaptura.generarLineaCaptura());
        cita.setMonto(servicioLineaCaptura.asignarPrecioCita());
        cita.setFechaVencimiento(fechaCita.toLocalDate());

        citaRepository.save(cita);
        return cita;
    }

    @Transactional
    public Cita buscarCitaPendienteMasReciente(Paciente paciente) {
        Cita cita = citaRepository.findTopByPacienteAndEstadoCitaOrderByFechaCitaAsc(paciente, TipoConfirmacionCita.PENDIENTE);

        if (cita != null) {
            if (cita.getPaciente() != null) {
                cita.getPaciente().getNombre(); // "Despierta" al paciente
            }
            if (cita.getPsicologo() != null) {
                cita.getPsicologo().getNombre(); // "Despierta" al psicólogo
            }
        }

        return cita;
    }

    @Transactional
    public List<Cita> listarCitasPorPaciente(Paciente paciente) {
        List<Cita> citas = citaRepository.findByPaciente(paciente);

        // Forzamos la carga de datos para evitar LazyInitializationException
        for (Cita cita : citas) {
            if (cita.getPaciente() != null) cita.getPaciente().getNombre();
            if (cita.getPsicologo() != null) cita.getPsicologo().getNombre();
        }
        return citas;
    }
}