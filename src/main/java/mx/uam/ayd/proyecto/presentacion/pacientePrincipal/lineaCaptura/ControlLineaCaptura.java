package mx.uam.ayd.proyecto.presentacion.pacientePrincipal.lineaCaptura;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.annotation.PostConstruct;
import javafx.scene.control.Alert;
import mx.uam.ayd.proyecto.negocio.ServicioLineaCaptura;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.ControlPaciente;

/**
 * Controlador para la Línea de Captura.
 * Es responsable de la generación, el registro del pago 
 * y la presentación del comprobante al paciente.
 */
@Component
public class ControlLineaCaptura {

    private final VentanaLineaCaptura ventanaLineaCaptura;
    private final ServicioLineaCaptura servicioLineaCaptura;
    // Referencia al controlador principal
    private ControlPaciente controlPaciente;

    /**
     * Constructor inyectado por Spring para dependencias directas.
     */
    @Autowired
    public ControlLineaCaptura(
        VentanaLineaCaptura ventanaLineaCaptura, 
        ServicioLineaCaptura servicioLineaCaptura
    ){
        this.ventanaLineaCaptura = ventanaLineaCaptura;
        this.servicioLineaCaptura = servicioLineaCaptura;
    }
    
    @Autowired 
    public void setControlPaciente(ControlPaciente controlPaciente) {
        this.controlPaciente = controlPaciente;
    }

    /**
     * Método que se ejecuta inmediatamente después de la construcción e inyección de dependencias.
     * Se usa para enlazar la vista con este controlador.
     */
    @PostConstruct
    public void inicializa(){
        ventanaLineaCaptura.setControl(this); 
    }

    /**
     * Inicia la generación y registro de la línea de captura.
     * Contiene la lógica y el manejo de errores.
     */
    public void inicia(){
        try{
            String nombre = controlPaciente.getNombreUsuarioActivo();
            double total = servicioLineaCaptura.asignarPrecioCita();
            String linea = servicioLineaCaptura.generarLineaCaptura();
            String fecha = servicioLineaCaptura.fechaActual();
            
            servicioLineaCaptura.registrarPago(nombre, total, linea, fecha);

            ventanaLineaCaptura.setDatosComprobante(nombre, total, linea, fecha);
            ventanaLineaCaptura.muestra();
            
        } catch (Exception e){
            System.err.println("Error al generar o registrar la línea de captura: " + e.getMessage());
            ventanaLineaCaptura.mostrarAlerta(
                Alert.AlertType.ERROR, 
                "Error de Transacción", 
                "No se pudo generar ni registrar la línea de captura. Intente de nuevo o contacte a soporte."
            );
            
            // Si hay un error fatal, cerramos la ventana por seguridad (si no se cerró antes de mostrar la alerta)
            // Ya que el controlPaciente llama a inicia, lo ideal es no dejar esta ventana colgada.
            ventanaLineaCaptura.cerrar();
        }
    }
}
