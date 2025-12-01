package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import mx.uam.ayd.proyecto.negocio.modelo.Cita;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;

public class ServicioGenerarPDFCitaTest {
    private ServicioGenerarPDFCita servicio;

    private Paciente paciente;
    private Psicologo psicologo;
    private Cita cita;

    /** Inicialización de datos de prueba antes de cada test */
    @BeforeEach
    void setUp() {
        servicio = new ServicioGenerarPDFCita();

        paciente = mock(Paciente.class);
        psicologo = mock(Psicologo.class);
        cita = mock(Cita.class);

        when(paciente.getNombre()).thenReturn("Juan Perez");
        when(paciente.getTelefono()).thenReturn("5551234567");
        when(paciente.getCorreo()).thenReturn("juan@mail.com");

        when(psicologo.getNombre()).thenReturn("Dra. Ana López");
        when(psicologo.getTelefono()).thenReturn("5559876543");

        when(cita.getMotivo()).thenReturn("Consulta general");
        when(cita.getFechaCita()).thenReturn(LocalDateTime.of(2025, 5, 15, 14, 30));
    }

    @Test
    void testGenerarCitaPDFCaminoFeliz() throws Exception {

        // Dado que existen datos válidos de paciente, psicólogo y cita

        PdfContentByte canvasMock = mock(PdfContentByte.class);
        BaseFont fuenteMock = mock(BaseFont.class);

        try (
            MockedConstruction<PdfReader> mockedReader = Mockito.mockConstruction(PdfReader.class);
            MockedConstruction<PdfStamper> mockedStamper = Mockito.mockConstruction(PdfStamper.class,
                (mock, context) -> {
                    when(mock.getOverContent(1)).thenReturn(canvasMock);
                });
            MockedStatic<BaseFont> baseFontMock = Mockito.mockStatic(BaseFont.class)
        ) {

            baseFontMock.when(() ->
                BaseFont.createFont(anyString(), anyString(), anyBoolean()))
                .thenReturn(fuenteMock);

            // Cuando se ejecuta la generación del PDF
            servicio.generarCitaPDF(paciente, psicologo, cita, "salida.pdf");

            // Entonces se crea el PdfReader y PdfStamper
            assertEquals(1, mockedReader.constructed().size());
            assertEquals(1, mockedStamper.constructed().size());

            // se escribe texto en el PDF
            verify(canvasMock, atLeastOnce()).beginText();
            verify(canvasMock, atLeastOnce()).setTextMatrix(anyFloat(), anyFloat());
            verify(canvasMock, atLeastOnce()).showText(anyString());
            verify(canvasMock, atLeastOnce()).endText();

            //y se cierra el PDF correctamente
            verify(mockedStamper.constructed().get(0)).close();
            verify(mockedReader.constructed().get(0)).close();
        }
    }

    @Test
    void testGenerarCitaPDFDatosNulos() throws Exception {

        // Dado que el paciente y el psicólogo contienen algunos valores nulos
        when(paciente.getNombre()).thenReturn(null);
        when(psicologo.getTelefono()).thenReturn(null);

        PdfContentByte canvasMock = mock(PdfContentByte.class);
        BaseFont fuenteMock = mock(BaseFont.class);

        try (
            MockedConstruction<PdfReader> mockedReader = Mockito.mockConstruction(PdfReader.class);
            MockedConstruction<PdfStamper> mockedStamper = Mockito.mockConstruction(PdfStamper.class,
                (mock, context) -> when(mock.getOverContent(1)).thenReturn(canvasMock));
            MockedStatic<BaseFont> baseFontMock = Mockito.mockStatic(BaseFont.class)
        ) {

            baseFontMock.when(() ->
                BaseFont.createFont(anyString(), anyString(), anyBoolean()))
                .thenReturn(fuenteMock);

            // Cuando se genera el PDF con valores nulos
            servicio.generarCitaPDF(paciente, psicologo, cita, "salida.pdf");

            // Entonces no se lanza excepción
            verify(canvasMock, atLeastOnce()).showText(anyString());
        }
    }

    @Test
    void testGenerarCitaPDFErrorAlCrearPdf() throws Exception {
        
        // Dado que ocurre un error al crear el PdfReader

        try (MockedConstruction<PdfReader> mockedReader =
                     Mockito.mockConstruction(PdfReader.class,
                        (mock, context) -> {
                            throw new RuntimeException("Error al abrir PDF");
                        })) {


            // Cuando se ejecuta el servicio
            // Entonces la excepción es manejada internamente y no truena el sistema
            assertDoesNotThrow(() ->
                servicio.generarCitaPDF(paciente, psicologo, cita, "salida.pdf")
            );
        }
    }
}
