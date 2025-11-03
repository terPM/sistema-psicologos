package mx.uam.ayd.proyecto.negocio;

import mx.uam.ayd.proyecto.datos.RegistroEmocionalRepository;
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

        // Creamos un objeto de retorno simulado
        RegistroEmocional registroSimulado = new RegistroEmocional();
        registroSimulado.setEmocion(emocion);
        registroSimulado.setNota(nota);
        registroSimulado.setFecha(LocalDate.now());
        registroSimulado.setId(1L);

        // Le decimos al simulador qué hacer:
        // "Cuando llamen al método 'save' con cualquier objeto 'RegistroEmocional',
        // entonces retorna nuestro 'registroSimulado'"
        when(registroEmocionalRepository.save(any(RegistroEmocional.class))).thenReturn(registroSimulado);

        // --- ACT (Actuar) ---
        // Llamamos al método que queremos probar
        RegistroEmocional resultado = servicioRegistroemocional.guardarRegistro(emocion, nota);

        // --- ASSERT (Afirmar) ---
        // 1. Verificar que el resultado no sea nulo
        assertNotNull(resultado, "El registro guardado no debe ser nulo.");

        // 2. Verificar que los datos se asignaron correctamente
        assertEquals(emocion, resultado.getEmocion(), "La emoción debe ser la que se guardó.");
        assertEquals(nota, resultado.getNota(), "La nota debe ser la que se guardó.");
        assertNotNull(resultado.getFecha(), "La fecha debe haberse asignado.");

        // 3. Verificar que el repositorio SÍ fue llamado para guardar
        verify(registroEmocionalRepository, times(1)).save(any(RegistroEmocional.class));
    }

    @Test
    void testGuardarRegistroConEmocionVacia() {
        System.out.println("TEST: Probando guardar un registro con emoción vacía");

        // --- ARRANGE (Preparar) ---
        String emocionVacia = "  ";
        String nota = "Esta prueba no debe guardar";

        // --- ACT & ASSERT (Actuar y Afirmar) ---
        // Verificamos que el método lance la excepción esperada (IllegalArgumentException)
        Exception excepcion = assertThrows(IllegalArgumentException.class, () -> {
            servicioRegistroemocional.guardarRegistro(emocionVacia, nota);
        });

        // Verificamos el mensaje de error
        assertEquals("La emoción no puede estar vacía", excepcion.getMessage());

        // MUY IMPORTANTE: Verificamos que el repositorio NUNCA fue llamado
        verify(registroEmocionalRepository, never()).save(any());
    }

    @Test
    void testGuardarRegistroConEmocionNula() {
        System.out.println("TEST: Probando guardar un registro con emoción nula");

        // --- ARRANGE (Preparar) ---
        String emocionNula = null;
        String nota = "Esta prueba tampoco debe guardar";

        // --- ACT & ASSERT (Actuar y Afirmar) ---
        // Verificamos que el método lance la excepción esperada
        assertThrows(IllegalArgumentException.class, () -> {
            servicioRegistroemocional.guardarRegistro(emocionNula, nota);
        });

        // Verificamos que el repositorio NUNCA fue llamado
        verify(registroEmocionalRepository, never()).save(any());
    }

    @Test
    void testListarRegistros() {
        System.out.println("TEST: Probando listar registros existentes");

        // --- ARRANGE (Preparar) ---
        // Creamos una lista falsa que simulará la respuesta de la BD
        RegistroEmocional r1 = new RegistroEmocional();
        r1.setEmocion("Triste");
        List<RegistroEmocional> listaSimulada = List.of(r1);

        // Le decimos al simulador qué hacer:
        // "Cuando llamen al método 'findAll', entonces retorna nuestra 'listaSimulada'"
        when(registroEmocionalRepository.findAll()).thenReturn(listaSimulada);

        // --- ACT (Actuar) ---
        List<RegistroEmocional> resultado = servicioRegistroemocional.listarRegistros();

        // --- ASSERT (Afirmar) ---
        // 1. Verificar que la lista no sea nula
        assertNotNull(resultado, "La lista no debe ser nula.");

        // 2. Verificar que la lista tenga el tamaño esperado
        assertEquals(1, resultado.size(), "La lista debe contener 1 elemento.");

        // 3. Verificar que el contenido sea el correcto
        assertEquals("Triste", resultado.get(0).getEmocion());
    }

    @Test
    void testListarRegistrosCuandoNoHay() {
        System.out.println("TEST: Probando listar registros cuando la base está vacía");

        // --- ARRANGE (Preparar) ---
        // Le decimos al simulador que retorne una lista vacía
        when(registroEmocionalRepository.findAll()).thenReturn(Collections.emptyList());

        // --- ACT (Actuar) ---
        List<RegistroEmocional> resultado = servicioRegistroemocional.listarRegistros();

        // --- ASSERT (Afirmar) ---
        // 1. Verificar que la lista no sea nula
        assertNotNull(resultado, "La lista no debe ser nula.");

        // 2. Verificar que la lista esté vacía
        assertTrue(resultado.isEmpty(), "La lista debe estar vacía.");
    }
}