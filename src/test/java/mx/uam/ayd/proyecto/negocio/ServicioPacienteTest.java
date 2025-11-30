package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.datos.PacienteRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;

@ExtendWith(MockitoExtension.class)
class ServicioPacienteTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private ServicioPaciente servicioPaciente;

    // Constantes para pruebas
    private final String CONTRASENA_VALIDA = "pa_pass123";
    private final String USUARIO_VALIDO = "pa_user1";

    @Test
    void testAgregarPaciente() {
        // Caso 1: Agregar paciente exitosamente
        when(pacienteRepository.findByCorreo("test@correo.com")).thenReturn(null);
        when(pacienteRepository.findByUsuario(USUARIO_VALIDO)).thenReturn(null); // Asegurar que el usuario no existe

        Paciente pacienteGuardado = new Paciente();
        pacienteGuardado.setNombre("Juan");
        pacienteGuardado.setCorreo("test@correo.com");
        pacienteGuardado.setTelefono("123456789");
        pacienteGuardado.setEdad(30);
        pacienteGuardado.setUsuario(USUARIO_VALIDO);
        pacienteGuardado.setContrasena(CONTRASENA_VALIDA);

        when(pacienteRepository.save(any(Paciente.class))).thenReturn(pacienteGuardado);

        // Prueba del método con los 6 argumentos
        Paciente result = servicioPaciente.agregarPaciente("Juan", "test@correo.com", "123456789", 30, USUARIO_VALIDO, CONTRASENA_VALIDA);

        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
        assertEquals("test@correo.com", result.getCorreo());
        assertEquals("123456789", result.getTelefono());
        assertEquals(30, result.getEdad());
        assertEquals(USUARIO_VALIDO, result.getUsuario());

        // Caso 2: Nombre nulo o vacío
        assertThrows(IllegalArgumentException.class, () -> {
            servicioPaciente.agregarPaciente(null, "correo@dominio.com", "123456789", 25, USUARIO_VALIDO, CONTRASENA_VALIDA);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            servicioPaciente.agregarPaciente(" ", "correo@dominio.com", "123456789", 25, USUARIO_VALIDO, CONTRASENA_VALIDA);
        });

        // Caso 3: Correo nulo o vacío
        assertThrows(IllegalArgumentException.class, () -> {
            servicioPaciente.agregarPaciente("Juan", null, "123456789", 25, USUARIO_VALIDO, CONTRASENA_VALIDA);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            servicioPaciente.agregarPaciente("Juan", " ", "123456789", 25, USUARIO_VALIDO, CONTRASENA_VALIDA);
        });

        // Caso 4: Teléfono nulo o vacío
        assertThrows(IllegalArgumentException.class, () -> {
            servicioPaciente.agregarPaciente("Juan", "correo@dominio.com", null, 25, USUARIO_VALIDO, CONTRASENA_VALIDA);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            servicioPaciente.agregarPaciente("Juan", "correo@dominio.com", " ", 25, USUARIO_VALIDO, CONTRASENA_VALIDA);
        });

        // Caso 5: Usuario nulo o vacío
        assertThrows(IllegalArgumentException.class, () -> {
            servicioPaciente.agregarPaciente("Juan", "correo@dominio.com", "123456789", 25, null, CONTRASENA_VALIDA);
        });

        // Caso 6: Contraseña nula o vacía
        assertThrows(IllegalArgumentException.class, () -> {
            servicioPaciente.agregarPaciente("Juan", "correo@dominio.com", "123456789", 25, USUARIO_VALIDO, null);
        });

        // Caso 7: Correo duplicado
        when(pacienteRepository.findByCorreo("repetido@correo.com")).thenReturn(new Paciente());
        assertThrows(IllegalArgumentException.class, () -> {
            servicioPaciente.agregarPaciente("Pedro", "repetido@correo.com", "111222333", 40, "otroUser", CONTRASENA_VALIDA);
        });

        // Caso 8: Usuario duplicado
        // (Configuramos mocks para que pase el correo pero falle el usuario)
        when(pacienteRepository.findByCorreo("nuevo@correo.com")).thenReturn(null);
        when(pacienteRepository.findByUsuario("usuarioDuplicado")).thenReturn(new Paciente());

        assertThrows(IllegalArgumentException.class, () -> {
            servicioPaciente.agregarPaciente("Pedro", "nuevo@correo.com", "111222333", 40, "usuarioDuplicado", CONTRASENA_VALIDA);
        });
    }

    @Test
    void testRecuperarTodosLosPacientes() {
        // Caso 1: Lista vacía
        when(pacienteRepository.findAll()).thenReturn(Collections.emptyList());
        List<Paciente> pacientes = servicioPaciente.recuperarTodosLosPacientes();
        assertEquals(0, pacientes.size(), "La lista de pacientes debe tener tamaño 0");

        // Caso 2: Lista con pacientes
        Paciente p1 = new Paciente();
        Paciente p2 = new Paciente();
        when(pacienteRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        pacientes = servicioPaciente.recuperarTodosLosPacientes();
        assertEquals(2, pacientes.size(), "La lista de pacientes debe tener tamaño 2");
    }

    @Test
    void testAsignarPsicologo() {
        Paciente paciente = new Paciente();
        Psicologo psicologo = new Psicologo();

        servicioPaciente.asignarPsicologo(paciente, psicologo);

        assertEquals(psicologo, paciente.getPsicologo());
        verify(pacienteRepository).save(paciente);
    }

    @Test
    void testObtenerPacientePorUsuario() {
        final String USUARIO_EXISTENTE = "pa_user_test";
        final String USUARIO_NO_EXISTENTE = "usuario_fantasma";

        Paciente pacienteEsperado = new Paciente();
        pacienteEsperado.setUsuario(USUARIO_EXISTENTE);
        pacienteEsperado.setNombre("Test Nombre");

        // Caso 1: Usuario existente
        when(pacienteRepository.findByUsuario(USUARIO_EXISTENTE)).thenReturn(pacienteEsperado);

        Paciente result = servicioPaciente.obtenerPacientePorUsuario(USUARIO_EXISTENTE);

        assertNotNull(result);
        assertEquals(USUARIO_EXISTENTE, result.getUsuario());
        verify(pacienteRepository).findByUsuario(USUARIO_EXISTENTE);

        // Caso 2: Usuario no existente
        when(pacienteRepository.findByUsuario(USUARIO_NO_EXISTENTE)).thenReturn(null);

        result = servicioPaciente.obtenerPacientePorUsuario(USUARIO_NO_EXISTENTE);

        assertNull(result);
        verify(pacienteRepository).findByUsuario(USUARIO_NO_EXISTENTE);
    }

    @Test
    void testActualizarPaciente() {
        Paciente paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNombre("Original");

        servicioPaciente.actualizarPaciente(paciente);

        verify(pacienteRepository).save(paciente);
    }
}