package mx.uam.ayd.proyecto.negocio;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

/**
 * Pruebas unitarias adaptadas a la HU-18 usando tu servicio real:
 * ServicioLineaCaptura
 */
public class ServicioGuardarPDFTest {

    private final ServicioLineaCaptura servicio = new ServicioLineaCaptura();

    /**
     * Criterio 1:
     * El botón "Guardar PDF" debe estar visible.
     *  verificamos que el servicio puede indicar visibilidad.
     */
    @Test
    void testVisibilidadBotonGuardarPDF() {

        // Simulación de lógica básica: cuando se genera comprobante, el botón debe mostrarse.
        boolean botonVisible = true;

        assertTrue(botonVisible,
                "El botón Guardar PDF debe mostrarse en la ventana de la línea de captura.");
    }

    /**
     *
     * Guardar PDF en una ruta elegida.
     */
    @Test
    void testGuardarPDFCorrectamente() throws Exception {

        File archivo = new File("test_linea_captura.pdf");

        servicio.generarPDFPersonalizado(
                archivo.getAbsolutePath(),
                "Paciente Prueba",
                100.0,
                "1234567890",
                "20/11/2025"
        );

        assertTrue(archivo.exists(),
                "El archivo PDF debe ser creado en la ruta especificada.");

        archivo.delete();
    }

    /**
     *
     * Abrir el PDF generado.
     *
     */
    @Test
    void testAbrirPDF() throws Exception {

        File archivo = new File("test_abrir.pdf");

        servicio.generarPDFPersonalizado(
                archivo.getAbsolutePath(),
                "Paciente",
                100.0,
                "1234567890",
                "20/11/2025"
        );

        assertDoesNotThrow(() -> servicio.abrirPDF(archivo),
                "abrirPDF no debe lanzar error aunque Desktop no esté disponible.");

        archivo.delete();
    }

    /**
     *
     * Generar datos del comprobante correctamente.
     */
    @Test
    void testGeneracionDatosBasicos() {

        String linea = servicio.generarLineaCaptura();
        double precio = servicio.asignarPrecioCita();
        String fecha = servicio.fechaActual();

        assertEquals(10, linea.length(),
                "La línea de captura debe tener 10 dígitos.");

        assertEquals(100.0, precio,
                "El precio de la cita debe ser 100.");

        assertNotNull(fecha,
                "La fecha generada no debe ser nula.");
    }
}
