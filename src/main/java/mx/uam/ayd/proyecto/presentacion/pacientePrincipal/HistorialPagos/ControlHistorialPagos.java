package mx.uam.ayd.proyecto.presentacion.pacientePrincipal.HistorialPagos;

import jakarta.annotation.PostConstruct;
import mx.uam.ayd.proyecto.negocio.ServicioCita;
import mx.uam.ayd.proyecto.negocio.modelo.Cita;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ControlHistorialPagos {

    @Autowired
    private VentanaHistorialPagos ventanaHistorialPagos;

    @Autowired
    private ServicioCita servicioCita;

    @PostConstruct
    public void init() {
        ventanaHistorialPagos.setControl(this);
    }

    /**
     * Inicia el flujo pidiendo las citas del paciente
     * y mostrándolas en la ventana.
     */
    public void inicia(Paciente paciente) {
        List<Cita> citas = servicioCita.listarCitasPorPaciente(paciente);
        ventanaHistorialPagos.muestra(citas);
    }

    public void termina() {
        ventanaHistorialPagos.setVisible(false);
    }
}