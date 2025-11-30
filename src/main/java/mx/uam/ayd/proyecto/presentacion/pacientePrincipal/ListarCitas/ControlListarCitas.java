package mx.uam.ayd.proyecto.presentacion.pacientePrincipal.ListarCitas;

import java.io.File;
import java.util.List;
import java.awt.Desktop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.stage.FileChooser;
import mx.uam.ayd.proyecto.negocio.ServicioCita;
import mx.uam.ayd.proyecto.negocio.ServicioGenerarPDFCita;
import mx.uam.ayd.proyecto.negocio.modelo.Cita;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;

@Component
public class ControlListarCitas {

    @Autowired
    private VentanaListarCitas ventana;

    @Autowired
    private ServicioCita servicioCita;

    @Autowired
    private ServicioGenerarPDFCita servicioGenerarPDFCita;

    private String nombreUsuarioActivo;

    /**
     * Inicia la ventana de listar citas del paciente
     */
    public void inicia(String nombreUsuarioActivo) {
        this.nombreUsuarioActivo = nombreUsuarioActivo;

        List<Cita> citas = servicioCita.listarCitas(nombreUsuarioActivo);

        ventana.setControlador(this);
        ventana.muestra(this, citas);
    }

    /**
     * Cancela la cita seleccionada desde la ventana
     */
    public void cancelarCita() {

        Integer idCita = ventana.getIdCitaSeleccionada();

        if (idCita == null) {
            ventana.muestraError("Debe seleccionar una cita para cancelar.");
            return;
        }

        boolean confirmado = ventana.muestraConfirmacion(
                "Confirmar cancelación",
                "¿Está seguro de cancelar la cita seleccionada?"
        );

        if (!confirmado) {
            return;
        }

        try {
            servicioCita.cancelarCita(idCita);
            ventana.muestraExito("La cita fue cancelada correctamente.");

            // Recargar tabla con citas actualizadas
            List<Cita> citas = servicioCita.listarCitas(nombreUsuarioActivo);
            ventana.cargarCitas(citas);

        } catch (Exception e) {
            ventana.muestraError("Error al cancelar cita: " + e.getMessage());
        }
    }

    /**
     * Permite guardar el archivo PDF generado
     * en una ubicacion seleccionada por el usuario
     * @return File con la ubicacion donde se guardo el PDF
     */
    private File guardar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar comprobante de cita");

        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("PDF Document (*.pdf)", "*.pdf")
        );

        fileChooser.setInitialFileName("Cita.pdf");

        return fileChooser.showSaveDialog(null);
    }

    /**
     * Genera el PDF de la cita seleccionada
     */
    public void generarPDFCita() {

        Integer idCita = ventana.getIdCitaSeleccionada();
        if (idCita == null) {
            ventana.muestraError("Seleccione una cita");
            return;
        }

        // Elegir dónde guardar
        File archivo = guardar();

        if (archivo == null) {
            return; // usuario canceló
        }

        // Obtener los datos de la cita seleccionada
        Cita cita = servicioCita.obtenerCitaPorId(idCita);
        Paciente paciente = cita.getPaciente();
        Psicologo psicologo = cita.getPsicologo();

        // Generar
        servicioGenerarPDFCita.generarCitaPDF(paciente, psicologo, cita, archivo.getAbsolutePath());

        ventana.muestraExito("PDF generado correctamente");

        abrirPDF(archivo);
    }

    /**
     * Abre el archivo PDF generado
     * @param archivo
     */
    private void abrirPDF(File archivo) {
        try {
            Desktop.getDesktop().open(archivo);
        } catch (Exception e) {
            ventana.muestraError("No se pudo abrir el PDF");
        }
    }


}
