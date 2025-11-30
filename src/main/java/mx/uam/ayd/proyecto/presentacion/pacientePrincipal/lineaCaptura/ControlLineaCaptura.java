package mx.uam.ayd.proyecto.presentacion.pacientePrincipal.lineaCaptura;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.negocio.ServicioLineaCaptura;
import mx.uam.ayd.proyecto.negocio.modelo.Cita;

@Component
public class ControlLineaCaptura {

    @Autowired
    private ServicioLineaCaptura servicioLineaCaptura;

    @Autowired
    private VentanaLineaCaptura ventanaLineaCaptura;

    /**
     * Recibe una cita pendiente desde ControlPaciente.
     */
    public void inicia(Cita cita) {

        if (cita == null || cita.getPaciente() == null) {
            System.err.println("Error: cita o paciente es null en ControlLineaCaptura");
            return;
        }

        String nombrePaciente = cita.getPaciente().getUsuario(); // como usa todo tu sistema
        double total = servicioLineaCaptura.asignarPrecioCita();
        String linea = servicioLineaCaptura.generarLineaCaptura();
        String fecha = servicioLineaCaptura.fechaActual();

        ventanaLineaCaptura.setDatosComprobante(nombrePaciente, total, linea, fecha);
        ventanaLineaCaptura.muestra();
    }
}
