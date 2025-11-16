package mx.uam.ayd.proyecto.negocio;

import mx.uam.ayd.proyecto.datos.RegistroEmocionalRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente; // <-- 1. Importar Paciente
import mx.uam.ayd.proyecto.negocio.modelo.RegistroEmocional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase ServicioRegistroemocional.
 * Verifica que la lógica de negocio funcione correctamente, aislando
 * la capa de persistencia (Repositorio).
 */
@ExtendWith(MockitoExtension.class) // Habilita Mockito para simular dependencias
public class ServicioRegistroemocionalTest {

    @Mock // 1. Creamos un simulador (Mock) del Repositorio
    private RegistroEmocionalRepository registroEmocionalRepository;

    @InjectMocks // 2. Inyectamos el simulador en el Servicio que probamos
    private ServicioRegistroemocional servicioRegistroemocional;

    @Test
    void testGuardarRegistroExitoso() {
        System.out.println("TEST: Probando guardar un registro exitosamente");

        // --- ARRANGE (Preparar) ---
        String emocion = "Feliz";
        String nota = "Prueba de nota";

        // --- CAMBIO: Crear un paciente simulado ---
        Paciente mockPaciente = new Paciente();
        mockPaciente.setId(1L);
        mockPaciente.setNombre("Paciente de Prueba");

        // Creamos un objeto de retorno simulado
        RegistroEmocional registroSimulado = new RegistroEmocional();
        registroSimulado.setEmocion(emocion);
        registroSimulado.setNota(nota);
        registroSimulado.setFecha(LocalDate.now());
        registroSimulado.setId(1L);
        registroSimulado.setPaciente(mockPaciente); // Asignamos el paciente

        // Le decimos al simulador qué hacer:
        when(registroEmocionalRepository.save(any(RegistroEmocional.class))).thenReturn(registroSimulado);

        // --- ACT (Actuar) ---
        // --- CAMBIO: Añadir el paciente a la llamada ---
        RegistroEmocional resultado = servicioRegistroemocional.guardarRegistro(emocion, nota, mockPaciente);

        // --- ASSERT (Afirmar) ---
        assertNotNull(resultado, "El registro guardado no debe ser nulo.");
        assertEquals(emocion, resultado.getEmocion(), "La emoción debe ser la que se guardó.");
        assertEquals(nota, resultado.getNota(), "La nota debe ser la que se guardó.");
        assertNotNull(resultado.getFecha(), "La fecha debe haberse asignado.");
        assertEquals(mockPaciente, resultado.getPaciente(), "El paciente debe ser el que se guardó."); // <-- 3. Verificar paciente

        // 4. Verificar que el repositorio SÍ fue llamado para guardar
        verify(registroEmocionalRepository, times(1)).save(any(RegistroEmocional.class));
    }

    @Test
    void testGuardarRegistroConEmocionVacia() {
        System.out.println("TEST: Probando guardar un registro con emoción vacía");

        // --- ARRANGE (Preparar) ---
        String emocionVacia = "  ";
        String nota = "Esta prueba no debe guardar";
        Paciente mockPaciente = new Paciente(); // Paciente simulado

        // --- ACT & ASSERT (Actuar y Afirmar) ---
        Exception excepcion = assertThrows(IllegalArgumentException.class, () -> {
            // --- CAMBIO: Añadir el paciente a la llamada ---
            servicioRegistroemocional.guardarRegistro(emocionVacia, nota, mockPaciente);
        });

        assertEquals("La emoción no puede estar vacía", excepcion.getMessage());
        verify(registroEmocionalRepository, never()).save(any());
    }

    @Test
    void testGuardarRegistroConEmocionNula() {
        System.out.println("TEST: Probando guardar un registro con emoción nula");

        // --- ARRANGE (Preparar) ---
        String emocionNula = null;
        String nota = "Esta prueba tampoco debe guardar";
        Paciente mockPaciente = new Paciente(); // Paciente simulado

        // --- ACT & ASSERT (Actuar y Afirmar) ---
        assertThrows(IllegalArgumentException.class, () -> {
            // --- CAMBIO: Añadir el paciente a la llamada ---
            servicioRegistroemocional.guardarRegistro(emocionNula, nota, mockPaciente);
        });

        verify(registroEmocionalRepository, never()).save(any());
    }

    // --- PRUEBA NUEVA: Para la lógica que añadimos ---
    @Test
    void testGuardarRegistroConPacienteNulo() {
        System.out.println("TEST: Probando guardar un registro con paciente nulo");

        // --- ARRANGE (Preparar) ---
        String emocion = "Feliz";
        String nota = "No importa";
        Paciente pacienteNulo = null;

        // --- ACT & ASSERT (Actuar y Afirmar) ---
        Exception excepcion = assertThrows(IllegalArgumentException.class, () -> {
            servicioRegistroemocional.guardarRegistro(emocion, nota, pacienteNulo);
        });

        assertEquals("El paciente no puede ser nulo", excepcion.getMessage());
        verify(registroEmocionalRepository, never()).save(any());
    }
    // --- FIN DE PRUEBA NUEVA ---


    @Test
    void testListarRegistros() {
        System.out.println("TEST: Probando listar registros existentes");

        // (Esta prueba no necesita cambios ya que no llama a guardarRegistro)

        // --- ARRANGE (Preparar) ---
        RegistroEmocional r1 = new RegistroEmocional();
        r1.setEmocion("Triste");
        List<RegistroEmocional> listaSimulada = List.of(r1);

        when(registroEmocionalRepository.findAll()).thenReturn(listaSimulada);

        // --- ACT (Actuar) ---
        List<RegistroEmocional> resultado = servicioRegistroemocional.listarRegistros();

        // --- ASSERT (Afirmar) ---
        assertNotNull(resultado, "La lista no debe ser nula.");
        assertEquals(1, resultado.size(), "La lista debe contener 1 elemento.");
        assertEquals("Triste", resultado.get(0).getEmocion());
    }

    @Test
    void testListarRegistrosCuandoNoHay() {
        System.out.println("TEST: Probando listar registros cuando la base está vacía");

        // (Esta prueba no necesita cambios)

        // --- ARRANGE (Preparar) ---
        when(registroEmocionalRepository.findAll()).thenReturn(Collections.emptyList());

        // --- ACT (Actuar) ---
        List<RegistroEmocional> resultado = servicioRegistroemocional.listarRegistros();

        // --- ASSERT (Afirmar) ---
        assertNotNull(resultado, "La lista no debe ser nula.");
        assertTrue(resultado.isEmpty(), "La lista debe estar vacía.");
    }
}