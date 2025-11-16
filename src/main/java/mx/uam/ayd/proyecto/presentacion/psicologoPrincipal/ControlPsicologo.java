package mx.uam.ayd.proyecto.presentacion.psicologoPrincipal;

import javafx.application.Platform;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;
import mx.uam.ayd.proyecto.presentacion.registrarNotas.ControlRegistrarNotas;
import mx.uam.ayd.proyecto.presentacion.principal.ControlPrincipalCentro;
import mx.uam.ayd.proyecto.presentacion.psicologoPrincipal.ListaRegistros.ControlListaRegistros;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ControlPsicologo {

    @Autowired
    private VentanaPsicologoPrincipal ventana;

    @Autowired
    private ControlRegistrarNotas controlRegistrarNotas;

    private ControlPrincipalCentro controlPrincipal;

    @Autowired
    private ControlListaRegistros controlListaRegistros;

    private Psicologo psicologoSesion;

    /**
     * Inicia el flujo principal del psicólogo
     */
    public void inicia(ControlPrincipalCentro controlPrincipal, Psicologo psicologo) {
        this.controlPrincipal = controlPrincipal;
        this.psicologoSesion = psicologo;
        ventana.setControlador(this);
        ventana.muestra();
    }

    public void registrarNotas() {
        controlRegistrarNotas.inicia();
    }

    /**
     * Inicia el flujo para ver la lista de registros
     */
    public void iniciarListaRegistros() {
        if (this.psicologoSesion != null) {
            controlListaRegistros.inicia(this.psicologoSesion);
        } else {
            System.err.println("No hay psicólogo en sesión para listar registros");
        }
    }

    public void salir() {
        ventana.oculta();
        this.psicologoSesion = null;
        if (controlPrincipal != null) {
            controlPrincipal.regresaAlLogin();
        } else {
            Platform.exit();
        }
    }
}