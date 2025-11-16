package mx.uam.ayd.proyecto.presentacion.pacientePrincipal;

import javafx.application.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Lazy;

import mx.uam.ayd.proyecto.negocio.ServicioCita;
import mx.uam.ayd.proyecto.negocio.modelo.Cita;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.RegistroEmocinal.ControlRegistroEmocinal;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.lineaCaptura.ControlLineaCaptura;
import mx.uam.ayd.proyecto.presentacion.principal.ControlPrincipalCentro;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.HistorialPagos.ControlHistorialPagos;

@Component
public class ControlPaciente {

    @Autowired
    private VentanaPacientePrincipal ventana;
    @Autowired
    private ControlRegistroEmocinal controlRegistroEmocinal;
    @Autowired
    @Lazy
    private ControlLineaCaptura controlLineaCaptura;
    @Autowired
    private ServicioCita servicioCita;

    @Autowired
    private ControlHistorialPagos controlHistorialPagos;

    private ControlPrincipalCentro controlPrincipal;
    private Paciente pacienteSesion;

    public void inicia(Paciente paciente, ControlPrincipalCentro controlPrincipal) {
        this.pacienteSesion = paciente;
        this.controlPrincipal = controlPrincipal;
        ventana.setControlador(this);
        ventana.muestra();
    }

    public Paciente getPacienteSesion() {
        return pacienteSesion;
    }

    public String getNombreUsuarioActivo() {
        return (pacienteSesion != null) ? pacienteSesion.getUsuario() : null;
    }

    public void salir() {
        ventana.oculta();
        this.pacienteSesion = null;
        if (controlPrincipal != null) {
            controlPrincipal.regresaAlLogin();
        } else {
            Platform.exit();
        }
    }

    public void iniciarRegistroEmocional() {
        if(this.pacienteSesion != null) {
            controlRegistroEmocinal.inicia(this.pacienteSesion);
        } else {
            System.err.println("No hay paciente en sesión para el registro emocional");
        }
    }

    public void iniciarLineaCaptura() {
        if (pacienteSesion == null) {
            System.err.println("No hay paciente en sesión");
            return;
        }

        Cita citaPendiente = servicioCita.buscarCitaPendienteMasReciente(pacienteSesion);

        if (citaPendiente != null) {
            controlLineaCaptura.inicia(citaPendiente);
        } else {
            ventana.muestraAviso("Sin Pagos Pendientes", "No tienes ninguna cita pendiente de pago.");
        }
    }

    public void iniciarHistorialPagos() {
        if (pacienteSesion == null) {
            System.err.println("No hay paciente en sesión");
            return;
        }
        controlHistorialPagos.inicia(pacienteSesion);
    }
}