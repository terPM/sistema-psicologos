package mx.uam.ayd.proyecto.negocio;

import org.springframework.stereotype.Service;

import java.awt.Desktop;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

@Service
public class ServicioLineaCaptura {

    public String generarLineaCaptura() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public double asignarPrecioCita() {
        return 100.00;
    }

    public String fechaActual() {
        LocalDate fecha = LocalDate.now();
        return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    /** Genera PDF en la ruta elegida */
    public File generarPDFPersonalizado(String ruta, String nombre, double total, String linea, String fecha) throws Exception {

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contenido = new PDPageContentStream(document, page);

        // Título
        contenido.setFont(PDType1Font.HELVETICA_BOLD, 16);
        contenido.beginText();
        contenido.newLineAtOffset(100, 700);
        contenido.showText("Comprobante de Pago");
        contenido.endText();

        contenido.setFont(PDType1Font.HELVETICA, 12);

        contenido.beginText();
        contenido.newLineAtOffset(100, 650);
        contenido.showText("Comprobante para: " + nombre);
        contenido.endText();

        contenido.beginText();
        contenido.newLineAtOffset(100, 620);
        contenido.showText("Total: $" + total);
        contenido.endText();

        contenido.beginText();
        contenido.newLineAtOffset(100, 590);
        contenido.showText("Línea de captura: " + linea);
        contenido.endText();

        contenido.beginText();
        contenido.newLineAtOffset(100, 560);
        contenido.showText("Fecha: " + fecha);
        contenido.endText();

        contenido.close();

        File file = new File(ruta);
        document.save(file);
        document.close();

        return file;
    }

    public void abrirPDF(File file) throws Exception {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(file);
        }
    }
}
