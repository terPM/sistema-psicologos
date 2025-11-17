package mx.uam.ayd.proyecto.presentacion.pacientePrincipal.lineaCaptura;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.annotation.PostConstruct;
import javafx.scene.control.Alert;
import mx.uam.ayd.proyecto.negocio.modelo.Cita;
import java.time.format.DateTimeFormatter;

@Component
public class ControlLineaCaptura {

    private final VentanaLineaCaptura ventanaLineaCaptura;

    @Autowired
    public ControlLineaCaptura(VentanaLineaCaptura ventanaLineaCaptura){
        this.ventanaLineaCaptura = ventanaLineaCaptura;
    }

    @PostConstruct
    public void inicializa(){
        ventanaLineaCaptura.setControl(this);
    }

    /**
     * Inicia el flujo para MOSTRAR la línea de captura de una cita.
     * @param cita La cita pendiente de pago.
     */
    public void inicia(Cita cita){
        try{
            String nombre = cita.getPaciente().getNombre();
            double total = cita.getMonto();
            String linea = cita.getLineaCaptura();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String fecha = cita.getFechaVencimiento().format(formatter);

            ventanaLineaCaptura.setDatosComprobante(nombre, total, linea, fecha);
            ventanaLineaCaptura.muestra();

        } catch (Exception e){
            System.err.println("Error al mostrar la línea de captura: " + e.getMessage());
            e.printStackTrace();
            ventanaLineaCaptura.mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error al Cargar",
                    "No se pudo mostrar la línea de captura."
            );
            ventanaLineaCaptura.cerrar();
        }
    }
}