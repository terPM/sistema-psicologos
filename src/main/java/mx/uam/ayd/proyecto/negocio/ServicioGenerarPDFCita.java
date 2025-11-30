package mx.uam.ayd.proyecto.negocio;

import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import mx.uam.ayd.proyecto.negocio.modelo.Cita;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;

@Service
public class ServicioGenerarPDFCita {

    /**
     * Genera un PDF de una cita usando una plantilla PDF.
     */
    public void generarCitaPDF(Paciente paciente, Psicologo psicologo, Cita cita, String rutaSalida) {
        try {
            LocalDateTime fechaCita = cita.getFechaCita();
            String fecha = fechaCita.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String hora = fechaCita.format(DateTimeFormatter.ofPattern("HH:mm"));

            String plantilla = "src\\main\\resources\\Plantilla_Cita.pdf";

            PdfReader reader = new PdfReader(plantilla);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(rutaSalida));

            PdfContentByte canvas = stamper.getOverContent(1);
            BaseFont fuente = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            canvas.setFontAndSize(fuente, 12);
            //Datos del paciente
            escribir(canvas, 134, 643, paciente.getNombre());
            escribir(canvas, 135, 608, paciente.getTelefono());
            escribir(canvas, 190, 574, paciente.getCorreo());
            //Datos del psicólogo
            escribir(canvas, 135, 503, psicologo.getNombre());
            escribir(canvas, 136, 468, psicologo.getTelefono());
            //Datos de la cita
            escribir(canvas, 122, 399, fecha);
            escribir(canvas, 227, 399, hora);
            escribir(canvas, 175, 365, cita.getMotivo());

            stamper.close();
            reader.close();

            System.out.println("PDF generado en: " + rutaSalida);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Escribe texto en el PDF en las coordenadas especificadas.
     */
    private void escribir(PdfContentByte canvas, float x, float y, String texto) {
        canvas.beginText();
        canvas.setTextMatrix(x, y);
        canvas.showText(texto != null ? texto : "");
        canvas.endText();
    }
}
