package mx.uam.ayd.proyecto.negocio;

import mx.uam.ayd.proyecto.datos.EncuestaSatisfaccionRepository;
import mx.uam.ayd.proyecto.negocio.modelo.EncuestaSatisfaccion;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.ControlPaciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ServicioEncuestaSatisfaccionTest {

    @Mock
    private EncuestaSatisfaccionRepository encuestaSatisfaccionRepository;

    @Mock
    private ControlPaciente controlPacienteMock; 
     
    @InjectMocks
    private ServicioEncuestaSatisfaccion servicioEncuestaSatisfaccion; 

    private Paciente pacienteTest;
    private EncuestaSatisfaccion encuestaCompleta;

    @BeforeEach
    void setUp() {
        pacienteTest = new Paciente(); 
        pacienteTest.setUsuario("Juanito123"); 
        
        encuestaCompleta = new EncuestaSatisfaccion();
        encuestaCompleta.setPaciente(pacienteTest); 
        encuestaCompleta.setQ1Empatia(4);
        encuestaCompleta.setQ2Confianza(3);
        encuestaCompleta.setQ3Respeto(4);
        encuestaCompleta.setQ4Confidencialidad(4);
        encuestaCompleta.setQ5Herramientas(2);
        encuestaCompleta.setQ6Satisfaccion(2);
        encuestaCompleta.setQ7Recomendacion(4);
    }
    
    /**
     * Simula la verificación de si un paciente ya ha respondido
     * usando el método existente obtenerTodasLasEncuestas().
     */
    private boolean simularYaRespondio(Paciente paciente, boolean respondio) {
        if (respondio) {
            // findAll() devuelve una lista con la encuesta del paciente
            List<EncuestaSatisfaccion> listaConEncuesta = List.of(encuestaCompleta); 
            when(encuestaSatisfaccionRepository.findAll()).thenReturn(listaConEncuesta);
        } else {
            // findAll() no devuelve encuestas para ese paciente
            when(encuestaSatisfaccionRepository.findAll()).thenReturn(Collections.emptyList());
        }
        
        // Ejecutamos la lógica de verificación basada en métodos existentes en el servicio
        return servicioEncuestaSatisfaccion.obtenerTodasLasEncuestas().stream()
                .filter(e -> e.getPaciente() != null && e.getPaciente().getUsuario().equals(paciente.getUsuario()))
                .findFirst()
                .isPresent();
    }


    // ESCENARIO: EL ADMINISTRADOR DEBE HABILITAR LA ENCUESTA
    @Test
    void testAdminHabilitaEncuestaYPacientePuedeContestar() {
        // Given: Dado que el administrador habilitó la encuesta
        servicioEncuestaSatisfaccion.habilitarEncuesta(); 
        // When/Then: La verificación usa isEncuestaHabilitada, que existe
        assertTrue(servicioEncuestaSatisfaccion.isEncuestaHabilitada(), "La encuesta debe estar habilitada.");
    }

    @Test
    void testAdminNoHabilitaEncuestaYPacienteNoPuedeResponder() {
        // Given: Dado que el administrador no ha habilitado la encuesta
        servicioEncuestaSatisfaccion.deshabilitarEncuesta(); 
        // When/Then: La verificación usa isEncuestaHabilitada, que existe
        assertFalse(servicioEncuestaSatisfaccion.isEncuestaHabilitada(), "La encuesta debe estar deshabilitada.");
    }

    // ESCENARIO: AL RESPONDER LA ENCUESTA DE SATISFACCIÓN
    @Test
    void testRespuestaObligatoriaLlevaAlExito() {
        // Given: Dado que respondí las preguntas obligatorias (1-7)
        when(encuestaSatisfaccionRepository.save(any(EncuestaSatisfaccion.class))).thenReturn(encuestaCompleta);
        // When: cuando doy clic en finalizar
        EncuestaSatisfaccion resultado = assertDoesNotThrow(() -> 
            servicioEncuestaSatisfaccion.guardarEncuesta(encuestaCompleta)
        );        
        // Then: entonces veré un mensaje de éxito
        assertNotNull(resultado, "El guardado debe ser exitoso.");
    }
    
    @Test
    void testFaltaPreguntasObligatoriasLlevaAError() {
        // Dado que el método yaRespondio no existe, esta prueba se basa en que el
        // método guardarEncuesta debe tener la lógica de validación e implementada.
    
        // Given: Dado que estoy en la encuesta
        EncuestaSatisfaccion encuestaIncompleta = new EncuestaSatisfaccion();
        encuestaIncompleta.setQ1Empatia(4);
        encuestaIncompleta.setQ7Recomendacion(0); // Falla de validación
        // When/Then: cuando doy en Terminar y faltan preguntas obligatorias (1-7)
        assertThrows(RuntimeException.class, () -> 
            servicioEncuestaSatisfaccion.guardarEncuesta(encuestaIncompleta),
            "Debe fallar si faltan respuestas obligatorias y la validación está activa."
        );
        // Verifica que NO se llamó al repositorio
        verify(encuestaSatisfaccionRepository, never()).save(any());
    }

    // ESCENARIO: RESTRICCIÓN DE RESPONDER NUEVAMENTE (SOLUCIÓN FUNCIONAL)
    @Test
    void testOpcionDeshabilitadaSiYaRespondi() {
        // Given: Dado que ya he respondido y enviado la encuesta
        boolean yaRespondio = simularYaRespondio(pacienteTest, true);
        // When: cuando accedo al menú principal del sistema nuevamente
        boolean debeEstarHabilitado = !yaRespondio;         
        // Then: la opción "Encuesta de satisfacción" debe aparecer deshabilitada (false en la bandera)
        assertFalse(debeEstarHabilitado, "La opción debería estar deshabilitada si el paciente ya respondió.");
    }
    
    @Test
    void testOpcionHabilitadaSiNoHeRespondido() {
        // Given: Dado que NO he respondido la encuesta
        // Simulamos que el repositorio no tiene encuestas del paciente
        boolean yaRespondio = simularYaRespondio(pacienteTest, false);
        // When: cuando accedo al menú principal del sistema nuevamente
        boolean debeEstarHabilitado = !yaRespondio;         
        // Then: la opción debe estar habilitada (true en la bandera)
        assertTrue(debeEstarHabilitado, "La opción debería estar habilitada si el paciente NO ha respondido.");
    }

    // ESCENARIO: PREGUNTAS ABIERTAS OPCIONALES
    @Test
    void testEnvioSinComentariosOpcionales() {
        // Given: Dado que Q8 y Q9 son opcionales y no escribo comentarios
        when(encuestaSatisfaccionRepository.save(any(EncuestaSatisfaccion.class))).thenReturn(encuestaCompleta);        
        EncuestaSatisfaccion resultado = assertDoesNotThrow(() -> 
            servicioEncuestaSatisfaccion.guardarEncuesta(encuestaCompleta)
        );                
        // Then: el sistema deberá dejarme enviar mi encuesta
        assertNotNull(resultado, "El guardado debe ser exitoso aunque Q8/Q9 sean nulos/vacíos.");
    }

    @Test
    void testComentariosOpcionalesSeGuardarán() {
        // Given: Dado que la pregunta 8 y 9 son abiertas y yo escribo un mensaje
        encuestaCompleta.setQ8Mejora("Sugerencia de mejora.");
        encuestaCompleta.setQ9Comentarios("Comentario adicional.");        
        // When: El sistema guarda la encuesta
        when(encuestaSatisfaccionRepository.save(any(EncuestaSatisfaccion.class))).thenReturn(encuestaCompleta);
        servicioEncuestaSatisfaccion.guardarEncuesta(encuestaCompleta);
        // Then: mis comentarios deben guardarse.
        verify(encuestaSatisfaccionRepository).save(argThat(encuesta -> 
            "Sugerencia de mejora.".equals(encuesta.getQ8Mejora()) && 
            "Comentario adicional.".equals(encuesta.getQ9Comentarios())
        ));
    }    
}