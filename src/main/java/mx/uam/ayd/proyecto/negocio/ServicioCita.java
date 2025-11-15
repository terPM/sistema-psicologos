package mx.uam.ayd.proyecto.negocio;

import java.time.LocalDateTime;
import java.util.List;

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
    
}
