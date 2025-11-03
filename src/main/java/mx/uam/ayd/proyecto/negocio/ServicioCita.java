package mx.uam.ayd.proyecto.negocio;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.CitaRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Cita;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;
import mx.uam.ayd.proyecto.negocio.modelo.TipoConfirmacionCita;

/**
 * Servicio relacionado con las citas
 * 
 * @author TechSolutions
 */
@Service
public class ServicioCita {
    
    @Autowired
    private CitaRepository citaRepository;

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
        
        citaRepository.save(cita);
        return cita;
    }
    
}
