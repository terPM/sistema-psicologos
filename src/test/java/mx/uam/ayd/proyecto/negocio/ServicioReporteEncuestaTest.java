package mx.uam.ayd.proyecto.negocio;

import mx.uam.ayd.proyecto.datos.EncuestaSatisfaccionRepository;
import mx.uam.ayd.proyecto.negocio.modelo.EncuestaSatisfaccion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServicioReporteEncuestaTest {

    @Mock
    private EncuestaSatisfaccionRepository encuestaSatisfaccionRepository;

    @InjectMocks
    private ServicioReporteEncuesta servicioReporteEncuesta;

    private List<EncuestaSatisfaccion> encuestasDePrueba;

    @BeforeEach
    void setUp() {
        // Configuración de datos de prueba
        EncuestaSatisfaccion e1 = new EncuestaSatisfaccion();
        e1.setQ1Empatia(4); 
        e1.setQ3Respeto(3); 
        e1.setQ7Recomendacion(1); 
        e1.setQ8Mejora("Sugerencia valida.");
        e1.setQ9Comentarios(""); 

        EncuestaSatisfaccion e2 = new EncuestaSatisfaccion();
        e2.setQ1Empatia(4); 
        e2.setQ3Respeto(3); 
        e2.setQ7Recomendacion(2); 
        e2.setQ8Mejora(null); 
        e2.setQ9Comentarios("Comentario final.");

        EncuestaSatisfaccion e3 = new EncuestaSatisfaccion();
        e3.setQ1Empatia(2); 
        e3.setQ3Respeto(4); 
        e3.setQ7Recomendacion(4); 
        e3.setQ8Mejora("  ");
        e3.setQ9Comentarios("  "); 

        EncuestaSatisfaccion e4 = new EncuestaSatisfaccion();
        e4.setQ1Empatia(0); 
        e4.setQ3Respeto(4); 
        e4.setQ7Recomendacion(4); 

        encuestasDePrueba = Arrays.asList(e1, e2, e3, e4);

        // Configuración del Mock: El repositorio siempre devuelve esta lista
        when(encuestaSatisfaccionRepository.findAll()).thenReturn(encuestasDePrueba);
    }

    // ESCENARIO 1: CONTEO CORRECTO 
    @Test
    void testConteoYTraduccionEtiquetas() {
        // Given: Datos de prueba con Q3: 3(x2), 4(x2) y Q3 en e4 es 4.        
        // When: Cuando se pide el conteo de la Pregunta 3 (Respeto)
        Map<String, Long> resultado = servicioReporteEncuesta.obtenerConteoRespuestas("Pregunta 3");
        // Then: El mapa debe reflejar el conteo y las etiquetas textuales.
        assertEquals(2, resultado.size(), "Debe haber 2 categorías distintas (Bueno y Excelente)");
        assertEquals(2L, resultado.get("Bueno"), "Dos encuestas deben ser Bueno.");
        assertEquals(2L, resultado.get("Excelente"), "Dos encuestas deben ser Excelente.");
        assertFalse(resultado.containsKey("Malo"), "No debe contener la etiqueta Malo.");
    }
    
    // ESCENARIO 2: FILTRADO DE RESPUESTAS INVÁLIDAS Y AUSENCIA DE DATOS
    @Test
    void testFiltradoDatosNulosYCero() {
        // Given: Los datos de prueba contienen Q1=0 (e4) y Q1=2 (e3).        
        // When: Cuando se pide el conteo de la Pregunta 1 (Empatía)
        Map<String, Long> resultado = servicioReporteEncuesta.obtenerConteoRespuestas("Pregunta 1");
        // Then: La encuesta con valor 0 debe ser ignorada y el conteo total debe ser 3.
        assertEquals(3L, resultado.values().stream().mapToLong(Long::longValue).sum(), "Debe contar 3 respuestas, ignorando el valor 0.");
        assertEquals(2L, resultado.get("Excelente"), "Debe haber 2 votos para Excelente.");        

        // When: Cuando se pide una pregunta que no está mapeada
        Map<String, Long> resultadoNoExistente = servicioReporteEncuesta.obtenerConteoRespuestas("Pregunta 99");        
        // Then: Debe devolver un mapa vacío
        assertTrue(resultadoNoExistente.isEmpty(), "Debe devolver un mapa vacío para pregunta no existente.");
    }

    // ESCENARIO 3: EXTRACCIÓN Y LIMPIEZA DE RESPUESTAS ABIERTAS (Q8 y Q9)

    @Test
    void testExtraccionYLimpiezaComentarios() {
        // Given: Datos de prueba con Q8 válido, nulo y solo espacios.        
        // When: Cuando se piden los comentarios de la Pregunta 8 (Sugerencia de Mejora)
        List<String> comentariosQ8 = servicioReporteEncuesta.obtenerRespuestasAbiertas("Pregunta 8");
        // Then: Solo deben devolverse los comentarios no nulos y no vacíos (e1).
        assertEquals(1, comentariosQ8.size(), "Debe devolver solo 1 comentario válido para Q8.");
        assertEquals("Sugerencia valida.", comentariosQ8.get(0));        
        
        // When: Cuando se piden los comentarios de la Pregunta 9 (Comentarios Adicionales)
        List<String> comentariosQ9 = servicioReporteEncuesta.obtenerRespuestasAbiertas("Pregunta 9");
        // Then: Solo deben devolverse los comentarios no nulos y no vacíos (e2).
        assertEquals(1, comentariosQ9.size(), "Debe devolver solo 1 comentario válido para Q9.");
        assertEquals("Comentario final.", comentariosQ9.get(0));
    }
    
    // ESCENARIO 4: REPORTE CON REPOSITORIO VACÍO
    @Test
    void testReporteConRepositorioVacio() {
        // Given: Se simula que el repositorio devuelve una lista completamente vacía
        when(encuestaSatisfaccionRepository.findAll()).thenReturn(Collections.emptyList());        
        // When: Cuando se pide el conteo de una pregunta cerrada (P1)
        Map<String, Long> resultadoCerrada = servicioReporteEncuesta.obtenerConteoRespuestas("Pregunta 1");
        // Then: El mapa de conteo debe estar vacío
        assertTrue(resultadoCerrada.isEmpty(), "El conteo debe estar vacío si no hay encuestas.");
        
        // When: Cuando se pide una pregunta abierta (P8)
        List<String> resultadoAbierta = servicioReporteEncuesta.obtenerRespuestasAbiertas("Pregunta 8");        
        // Then: La lista de comentarios debe estar vacía
        assertTrue(resultadoAbierta.isEmpty(), "La lista de comentarios debe estar vacía si no hay encuestas.");
    }
}
