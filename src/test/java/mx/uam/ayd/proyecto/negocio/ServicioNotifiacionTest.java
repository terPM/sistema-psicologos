package mx.uam.ayd.proyecto.negocio;

import mx.uam.ayd.proyecto.datos.NotificacionRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Notificacion;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServicioNotifiacionTest {

    @Mock
    private NotificacionRepository notificacionRepository;
    @InjectMocks
    private ServicioNotificacion servicioNotificacion;
    @Mock
    private Paciente pacienteMock;

    /**
     * Test para: crearNotificacionPaciente
     * Visualización de notificaciones no leídas (Burbuja Roja)
     */
    @Test
    public void testCrearNotificacionPaciente() {
        String mensaje = "Su cita es en 48 horas";
        
        // Ejecutamos el método
        servicioNotificacion.crearNotificacionPaciente(pacienteMock, mensaje);

        // Capturamos el argumento pasado al repositorio para inspeccionarlo
        ArgumentCaptor<Notificacion> notificacionCaptor = ArgumentCaptor.forClass(Notificacion.class);
        verify(notificacionRepository).save(notificacionCaptor.capture()); 

        Notificacion notificacionGuardada = notificacionCaptor.getValue();

        // Verificaciones
        assertNotNull(notificacionGuardada);
        assertEquals(pacienteMock, notificacionGuardada.getPaciente());
        assertEquals(mensaje, notificacionGuardada.getMensaje());
        assertNotNull(notificacionGuardada.getFecha()); // Validamos que se asigne fecha (LocalDateTime.now())
    }

    /**
     * Test para: contarNoLeidasPaciente
     * Visualización de notificaciones no leídas (Burbuja Roja)
     */
    @Test
    public void testContarNoLeidasPaciente() {
        // Configuramos el mock para simular que hay 3 notificaciones sin leer
        when(notificacionRepository.countByPacienteAndLeidaFalse(pacienteMock)).thenReturn(3L);

        // Ejecución
        long conteo = servicioNotificacion.contarNoLeidasPaciente(pacienteMock);

        // Verificación
        assertEquals(3L, conteo, "El conteo debe coincidir con lo retornado por el repositorio");
        verify(notificacionRepository).countByPacienteAndLeidaFalse(pacienteMock);
    }

    /**
     * Test para: obtenerTodasPorPaciente
     */
    @Test
    public void testObtenerTodasPorPaciente() {
        // Simulamos una lista de notificaciones
        Notificacion n1 = new Notificacion();
        Notificacion n2 = new Notificacion();
        List<Notificacion> listaSimulada = Arrays.asList(n1, n2);

        // Configuramos el mock para retornar la lista ordenada por fecha desc
        when(notificacionRepository.findByPacienteOrderByFechaDesc(pacienteMock)).thenReturn(listaSimulada);

        // Ejecución
        List<Notificacion> resultado = servicioNotificacion.obtenerTodasPorPaciente(pacienteMock);

        // Verificación
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(notificacionRepository).findByPacienteOrderByFechaDesc(pacienteMock);
    }

    /**
     * Test para: marcarTodasComoLeidasPaciente
     * Actualización del estado de notificación leída
     */
    @Test
    public void testMarcarTodasComoLeidasPaciente() {
        // 1. Preparamos datos de prueba: Notificaciones NO leídas
        Notificacion n1 = new Notificacion();
        n1.setLeida(false);
        Notificacion n2 = new Notificacion();
        n2.setLeida(false);
        
        List<Notificacion> noLeidas = Arrays.asList(n1, n2);

        // 2. Configuramos el mock para que devuelva estas notificaciones cuando se busquen las no leídas
        when(notificacionRepository.findByPacienteAndLeidaFalseOrderByFechaDesc(pacienteMock))
                .thenReturn(noLeidas);

        // 3. Ejecutamos el método a probar
        servicioNotificacion.marcarTodasComoLeidasPaciente(pacienteMock);

        // 4. Verificaciones
                assertTrue(n1.isLeida());
        assertTrue(n2.isLeida());
        verify(notificacionRepository, times(2)).save(any(Notificacion.class));
    }
    
}