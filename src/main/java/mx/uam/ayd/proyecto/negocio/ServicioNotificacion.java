package mx.uam.ayd.proyecto.negocio;

import mx.uam.ayd.proyecto.datos.NotificacionRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Notificacion;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ServicioNotificacion {

    @Autowired
    private NotificacionRepository notificacionRepository;

    public void crearNotificacion(Psicologo psicologo, String mensaje) {
        Notificacion n = new Notificacion(psicologo, mensaje, LocalDateTime.now());
        notificacionRepository.save(n);
    }

    /** Para la burbuja roja */
    public List<Notificacion> obtenerNoLeidas(Psicologo psicologo) {
        return notificacionRepository.findByPsicologoAndLeidaFalseOrderByFechaDesc(psicologo);
    }

    /** Si necesitas listar todas */
    public List<Notificacion> obtenerTodas(Psicologo psicologo) {
        return notificacionRepository.findByPsicologoOrderByFechaDesc(psicologo);
    }

    public long contarNoLeidas(Psicologo psicologo) {
        return notificacionRepository.countByPsicologoAndLeidaFalse(psicologo);
    }

    public void marcarTodasComoLeidas(Psicologo psicologo) {
        var noLeidas = obtenerNoLeidas(psicologo);
        noLeidas.forEach(n -> {
            n.setLeida(true);
            notificacionRepository.save(n);
        });
    }

    /**
     * Crea y guarda una notificación dirigida a un Paciente.
     * Se usará cuando el sistema detecte que falta poco para una cita.
     */
    public void crearNotificacionPaciente(Paciente paciente, String mensaje) {
        Notificacion n = new Notificacion(paciente, mensaje, LocalDateTime.now());
        notificacionRepository.save(n);
    }
    
    /**
     * Cuenta cuántas notificaciones tiene el paciente sin leer.
     * Sirve para determinar si se muestra la burbuja roja en la UI.
     * Requerimiento: 
     */
    public long contarNoLeidasPaciente(Paciente p) {
        return notificacionRepository.countByPacienteAndLeidaFalse(p);
    }

    /**
     * Obtiene todas las notificaciones del paciente ordenadas por fecha.
     * Requerimiento: "el orden de las notificaciones deberá ser de la más próxima a la última" 
     */
    public List<Notificacion> obtenerTodasPorPaciente(Paciente p) {
        return notificacionRepository.findByPacienteOrderByFechaDesc(p);
    }

    /**
     * Marca todas las notificaciones pendientes del paciente como leídas.
     * Requerimiento: "la burbuja roja deberá de dejar de aparecer" 
     */
    public void marcarTodasComoLeidasPaciente(Paciente p) {
        List<Notificacion> noLeidas = notificacionRepository.findByPacienteAndLeidaFalseOrderByFechaDesc(p);
        noLeidas.forEach(n -> {
            n.setLeida(true);
            notificacionRepository.save(n);
        });
    }
}
