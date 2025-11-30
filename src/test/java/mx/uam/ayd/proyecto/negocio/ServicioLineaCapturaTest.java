package mx.uam.ayd.proyecto.negocio;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Pruebas unitarias para la clase ServicioLineaCaptura.
 * Verifica que los métodos de generación de datos funcionen correctamente y de forma aislada.
 */
public class ServicioLineaCapturaTest {

    // Instancia del servicio que vamos a probar.
    private final ServicioLineaCaptura servicio = new ServicioLineaCaptura();

    @Test
    void testGenerarLineaCaptura() {
        System.out.println("TEST: Probando generación de Línea de Captura");
        
        String linea = servicio.generarLineaCaptura();
        
        // 1. Verificar que la línea no sea nula o vacía.
        assertNotNull(linea, "La línea de captura no debe ser nula.");
        assertFalse(linea.isEmpty(), "La línea de captura no debe estar vacía.");
        
        // 2. Verificar que tenga la longitud correcta (10 dígitos).
        assertEquals(10, linea.length(), "La línea de captura debe tener exactamente 10 caracteres.");
        
        // 3. Verificar que todos los caracteres sean dígitos.
        assertTrue(linea.matches("\\d+"), "La línea de captura debe contener solo dígitos.");
        
        // Prueba de aleatoriedad (probando que dos líneas sean diferentes, aunque puede fallar ocasionalmente)
        String otraLinea = servicio.generarLineaCaptura();
        assertNotEquals(linea, otraLinea, "Dos líneas consecutivas deben ser diferentes (probabilidad alta).");
    }

    @Test
    void testAsignarPrecioCita() {
        System.out.println("TEST: Probando asignación de precio de cita");
        
        double precio = servicio.asignarPrecioCita();
        
        // Verificar que el precio asignado es el valor esperado.
        assertEquals(100.00, precio, 0.001, "El precio de la cita debe ser 100.00.");
        
        // Verificar que el precio es positivo.
        assertTrue(precio > 0, "El precio de la cita debe ser un valor positivo.");
    }

    @Test
    void testFechaActual() {
        System.out.println("TEST: Probando formato de fecha actual");
        
        String fecha = servicio.fechaActual();
        
        // 1. Verificar que la fecha no sea nula.
        assertNotNull(fecha, "La fecha no debe ser nula.");
        
        // 2. Verificar que el formato de la fecha sea el esperado (dd/MM/yyyy).
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaEsperada = LocalDate.now().format(formatter);
        
        assertEquals(fechaEsperada, fecha, "El formato de la fecha debe ser dd/MM/yyyy y coincidir con el día actual.");
        
        // 3. Verificar la longitud (10 caracteres, ej: 02/11/2025).
        assertEquals(10, fecha.length(), "La longitud de la fecha debe ser 10 caracteres.");
    }
    @Test
    void testRegistrarPago() {
        System.out.println("TEST: Probando el Stub de registrarPago (smoke test)");
        
        // Dado que este método es un stub (no tiene persistencia real), 
        // solo verificamos que no lance una excepción al ser llamado.
        assertDoesNotThrow(() -> {
            servicio.registrarPago("TestPaciente", 100.00, "1234567890", "01/01/2025");
        }, "El método registrarPago no debe lanzar excepciones en la implementación actual (stub).");
    }
}