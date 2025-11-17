package mx.uam.ayd.proyecto.negocio;

import mx.uam.ayd.proyecto.datos.CitaRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Cita;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;
import mx.uam.ayd.proyecto.negocio.modelo.TipoConfirmacionCita;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase ServicioCita.
 * Verifica la lógica de crear citas y listar el historial de pagos.
 */
@ExtendWith(MockitoExtension.class)
public class ServicioCitaTest {

    @Mock // Creamos un simulador del Repositorio de Citas
    private CitaRepository citaRepository;

    @Mock // Creamos un simulador del Servicio de Línea de Captura
    private ServicioLineaCaptura servicioLineaCaptura;

    @InjectMocks // Inyectamos los simuladores en el Servicio real que probamos
    private ServicioCita servicioCita;

    @Test
    void testListarCitasPorPacienteExitoso() {
        System.out.println("TEST: Probando listar historial de pagos (citas) de un paciente");

        Paciente pacientePrueba = new Paciente();
        pacientePrueba.setId(1L);
        pacientePrueba.setNombre("Paciente Test");

        Psicologo psicoPrueba = new Psicologo();
        psicoPrueba.setId(1);
        psicoPrueba.setNombre("Psicologo Test");

        Cita cita1 = new Cita();
        cita1.setId(101);
        cita1.setMonto(100.00);
        cita1.setLineaCaptura("12345");
        cita1.setPaciente(pacientePrueba);
        cita1.setPsicologo(psicoPrueba);

        List<Cita> listaSimulada = List.of(cita1);

        when(citaRepository.findByPaciente(pacientePrueba)).thenReturn(listaSimulada);

        List<Cita> resultado = servicioCita.listarCitasPorPaciente(pacientePrueba);

        assertNotNull(resultado, "La lista no debe ser nula.");
        assertEquals(1, resultado.size(), "La lista debe contener 1 cita.");
        assertEquals("12345", resultado.get(0).getLineaCaptura(), "La línea de captura debe coincidir.");
        assertEquals(100.00, resultado.get(0).getMonto(), "El monto debe coincidir.");

        // Verificamos que el método del repositorio SÍ fue llamado
        verify(citaRepository, times(1)).findByPaciente(pacientePrueba);
    }

    @Test
    void testListarCitasPorPacienteVacio() {
        System.out.println("TEST: Probando historial de pagos de un paciente SIN citas");

        Paciente pacientePrueba = new Paciente();
        pacientePrueba.setId(2L);

        when(citaRepository.findByPaciente(pacientePrueba)).thenReturn(Collections.emptyList());

        List<Cita> resultado = servicioCita.listarCitasPorPaciente(pacientePrueba);

        assertNotNull(resultado, "La lista no debe ser nula.");
        assertTrue(resultado.isEmpty(), "La lista debe estar vacía.");
        verify(citaRepository, times(1)).findByPaciente(pacientePrueba);
    }

    @Test
    void testCrearCitaExitoso() {
        System.out.println("TEST: Probando crear una cita exitosamente");

        Paciente pacientePrueba = new Paciente();
        pacientePrueba.setPsicologo(new Psicologo());

        LocalDateTime fecha = LocalDateTime.now().plusDays(1);
        String motivo = "Test";

        when(citaRepository.findByFechaCita(fecha)).thenReturn(null); // No hay cita existente
        when(servicioLineaCaptura.generarLineaCaptura()).thenReturn("LINEA123");
        when(servicioLineaCaptura.asignarPrecioCita()).thenReturn(100.0);
        when(citaRepository.save(any(Cita.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cita resultado = servicioCita.crearCita(pacientePrueba, fecha, motivo);

        assertNotNull(resultado);
        assertEquals(pacientePrueba, resultado.getPaciente());
        assertEquals(fecha, resultado.getFechaCita());
        assertEquals(TipoConfirmacionCita.PENDIENTE, resultado.getEstadoCita());
        assertEquals("LINEA123", resultado.getLineaCaptura()); // Verificamos la línea
        assertEquals(100.0, resultado.getMonto()); // Verificamos el monto

        verify(citaRepository, times(1)).save(any(Cita.class));
    }

    @Test
    void testCrearCitaEnHorarioOcupado() {
        System.out.println("TEST: Probando crear una cita en horario ya ocupado");

        Paciente pacientePrueba = new Paciente();
        LocalDateTime fecha = LocalDateTime.now().plusDays(1);

        // Simulamos que ya existe una cita en esa fecha
        when(citaRepository.findByFechaCita(fecha)).thenReturn(new Cita());

        Exception excepcion = assertThrows(IllegalArgumentException.class, () -> {
            servicioCita.crearCita(pacientePrueba, fecha, "Test");
        });

        assertEquals("Ya existe una cita agendada en esta fecha y hora", excepcion.getMessage());

        verify(citaRepository, never()).save(any());
    }
}