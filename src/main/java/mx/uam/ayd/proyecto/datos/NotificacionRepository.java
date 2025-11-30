package mx.uam.ayd.proyecto.datos;

import mx.uam.ayd.proyecto.negocio.modelo.Notificacion;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotificacionRepository extends CrudRepository<Notificacion, Long> {

    // Notificaciones NO LEÍDAS (para la burbuja roja)
    List<Notificacion> findByPsicologoAndLeidaFalseOrderByFechaDesc(Psicologo psicologo);

    // Todas las notificaciones
    List<Notificacion> findByPsicologoOrderByFechaDesc(Psicologo psicologo);

    // Contador de no leídas
    long countByPsicologoAndLeidaFalse(Psicologo psicologo);


    // Notificaciones NO LEÍDAS del paciente (para obtener lista de pendientes específicas)
    List<Notificacion> findByPacienteAndLeidaFalseOrderByFechaDesc(Paciente paciente);

    // Todas las notificaciones del paciente ordenadas por fecha (para el historial)
    List<Notificacion> findByPacienteOrderByFechaDesc(Paciente paciente);

    // Contador de no leídas del paciente (para activar/desactivar burbuja roja)
    long countByPacienteAndLeidaFalse(Paciente paciente);
}