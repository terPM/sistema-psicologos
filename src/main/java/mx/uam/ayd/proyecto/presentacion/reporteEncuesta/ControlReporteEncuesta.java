package mx.uam.ayd.proyecto.presentacion.reporteEncuesta;

import mx.uam.ayd.proyecto.negocio.ServicioReporteEncuesta; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

/**
 * Controlador de Lógica de Negocio para el Reporte de Encuesta.
 * Coordina la comunicación entre la vista y el servicio.
 */
@Component
public class ControlReporteEncuesta {
    @Autowired
    private ServicioReporteEncuesta servicioReporteEncuesta; 
    @Autowired
    @Lazy 
    private VentanaReporteEncuesta ventana; 
    
    /**
     * Punto de entrada para mostrar la ventana (llamado desde ControlMenu).
     */
    public void inicia() {
        ventana.setControl(this);
        ventana.muestra();
    }
    
    /**
     * Llamado por la vista justo antes de mostrarse para cargar los datos iniciales.
     */
    public void cargarDatosIniciales() {
        mostrarComentarios("q9Comentarios");
    }

    /**
     * Obtiene y muestra los conteos de respuestas para una pregunta de opción múltiple.
     */
    public void mostrarGrafica(String pregunta) {
        // CORRECCIÓN APLICADA AQUÍ: Se cambia Map<Integer, Long> a Map<String, Long>
        Map<String, Long> conteos = servicioReporteEncuesta.obtenerConteoRespuestas(pregunta);
        
        // La ventana (VentanaReporteEncuesta) debe ser actualizada para recibir Map<String, Long>
        ventana.actualizarGrafica(pregunta, conteos);
    }
    
    /**
     * Obtiene y muestra los comentarios de texto para una pregunta abierta.
     */
    public void mostrarComentarios(String pregunta) {
        List<String> comentarios = servicioReporteEncuesta.obtenerRespuestasAbiertas(pregunta);
        ventana.actualizarListaComentarios(pregunta, comentarios);
    }
    
    /**
     * Cierra el flujo del reporte.
     */
    public void terminar() {
        ventana.cerrarVentana();
    }
}