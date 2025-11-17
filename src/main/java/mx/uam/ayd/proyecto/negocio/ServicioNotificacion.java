package mx.uam.ayd.proyecto.negocio;

import mx.uam.ayd.proyecto.datos.NotificacionRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Notificacion;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;
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
}
