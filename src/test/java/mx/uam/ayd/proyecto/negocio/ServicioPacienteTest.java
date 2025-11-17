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
    
    // --- Valores de prueba para los 2 String que faltaban ---
    private final String CONTRASENA_VALIDA = "pa_pass123";
    private final String USUARIO_VALIDO = "pa_user1";
    // --------------------------------------------------------

    @Test
    void testAgregarPaciente() {
        // Caso 1: Agregar paciente exitosamente
        when(pacienteRepository.findByCorreo("test@correo.com")).thenReturn(null);

        Paciente pacienteGuardado = new Paciente();
        pacienteGuardado.setNombre("Juan");
        pacienteGuardado.setCorreo("test@correo.com");
        pacienteGuardado.setTelefono("123456789");
        pacienteGuardado.setEdad(30);

        when(pacienteRepository.save(any(Paciente.class))).thenReturn(pacienteGuardado);

        // **CORREGIDO:** Se pasan los 6 argumentos (nombre, correo, telefono, edad, CONTRASENA_VALIDA, USUARIO_VALIDO)
        Paciente result = servicioPaciente.agregarPaciente("Juan", "test@correo.com", "123456789", 30, CONTRASENA_VALIDA, USUARIO_VALIDO);

        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
        assertEquals("test@correo.com", result.getCorreo());
        assertEquals("123456789", result.getTelefono());
        assertEquals(30, result.getEdad());

        // Caso 2: Nombre nulo o vacío
        assertThrows(IllegalArgumentException.class, () -> {
            // **CORREGIDO:** Se pasan los 6 argumentos
            servicioPaciente.agregarPaciente(null, "correo@dominio.com", "123456789", 25, CONTRASENA_VALIDA, USUARIO_VALIDO);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            // **CORREGIDO:** Se pasan los 6 argumentos
            servicioPaciente.agregarPaciente(" ", "correo@dominio.com", "123456789", 25, CONTRASENA_VALIDA, USUARIO_VALIDO);
        });

        // Caso 3: Correo nulo o vacío
        assertThrows(IllegalArgumentException.class, () -> {
            // **CORREGIDO:** Se pasan los 6 argumentos
            servicioPaciente.agregarPaciente("Juan", null, "123456789", 25, CONTRASENA_VALIDA, USUARIO_VALIDO);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            // **CORREGIDO:** Se pasan los 6 argumentos
            servicioPaciente.agregarPaciente("Juan", " ", "123456789", 25, CONTRASENA_VALIDA, USUARIO_VALIDO);
        });

        // Caso 4: Teléfono nulo o vacío
        assertThrows(IllegalArgumentException.class, () -> {
            // **CORREGIDO:** Se pasan los 6 argumentos
            servicioPaciente.agregarPaciente("Juan", "correo@dominio.com", null, 25, CONTRASENA_VALIDA, USUARIO_VALIDO);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            // **CORREGIDO:** Se pasan los 6 argumentos
            servicioPaciente.agregarPaciente("Juan", "correo@dominio.com", " ", 25, CONTRASENA_VALIDA, USUARIO_VALIDO);
        });

        // Caso 5: Correo duplicado
        when(pacienteRepository.findByCorreo("repetido@correo.com")).thenReturn(new Paciente());
        assertThrows(IllegalArgumentException.class, () -> {
            // **CORREGIDO:** Se pasan los 6 argumentos
            servicioPaciente.agregarPaciente("Pedro", "repetido@correo.com", "111222333", 40, CONTRASENA_VALIDA, USUARIO_VALIDO);
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
        // Valores de prueba
        final String USUARIO_EXISTENTE = "pa_user_test";
        final String USUARIO_NO_EXISTENTE = "usuario_fantasma";
        
        // Configuración de un paciente de prueba
        Paciente pacienteEsperado = new Paciente();
        pacienteEsperado.setUsuario(USUARIO_EXISTENTE);
        pacienteEsperado.setNombre("Test Nombre");
        // Establecer otros campos si es necesario para una verificación más completa
        pacienteEsperado.setCorreo("test@correo.com");
        pacienteEsperado.setEdad(35);
        pacienteEsperado.setContrasena(CONTRASENA_VALIDA);

        // Caso 1: Usuario existente
        // Simular que el repositorio encuentra al paciente
        when(pacienteRepository.findByUsuario(USUARIO_EXISTENTE)).thenReturn(pacienteEsperado);

        Paciente result = servicioPaciente.obtenerPacientePorUsuario(USUARIO_EXISTENTE);

        // Verificaciones
        assertNotNull(result, "Debe retornar un Paciente cuando el usuario existe.");
        assertEquals(USUARIO_EXISTENTE, result.getUsuario(), "El usuario del paciente debe coincidir.");
        assertEquals("Test Nombre", result.getNombre(), "El nombre del paciente debe ser correcto.");
        
        // Verificar que el método del repositorio fue llamado
        verify(pacienteRepository).findByUsuario(USUARIO_EXISTENTE);


        // Caso 2: Usuario no existente
        // Simular que el repositorio no encuentra al paciente (retorna null)
        when(pacienteRepository.findByUsuario(USUARIO_NO_EXISTENTE)).thenReturn(null);

        result = servicioPaciente.obtenerPacientePorUsuario(USUARIO_NO_EXISTENTE);

        // Verificaciones
        assertNull(result, "Debe retornar null cuando el usuario no existe.");
        
        // Verificar que el método del repositorio también fue llamado para este caso
        verify(pacienteRepository).findByUsuario(USUARIO_NO_EXISTENTE);
    }
}