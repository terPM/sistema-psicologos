package mx.uam.ayd.proyecto.datos;

import mx.uam.ayd.proyecto.negocio.modelo.Notificacion;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotificacionRepository extends CrudRepository<Notificacion, Long> {

    // Notificaciones NO LEÍDAS (para la burbuja roja)
    List<Notificacion> findByPsicologoAndLeidaFalseOrderByFechaDesc(Psicologo psicologo);

    // Todas las notificaciones
    List<Notificacion> findByPsicologoOrderByFechaDesc(Psicologo psicologo);

    // Contador de no leídas
    long countByPsicologoAndLeidaFalse(Psicologo psicologo);
}
