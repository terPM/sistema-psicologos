package mx.uam.ayd.proyecto.presentacion.pacientePrincipal.ListaRegistros;

import jakarta.annotation.PostConstruct;
import mx.uam.ayd.proyecto.negocio.ServicioRegistroemocional;
import mx.uam.ayd.proyecto.negocio.modelo.RegistroEmocional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Controlador para la ventana de Lista de Registros.
 */
@Component
public class ControlListaRegistros {

    @Autowired
    private VentanaListaRegistros ventanaListaRegistros;

    @Autowired
    private ServicioRegistroemocional servicioRegistroEmocional;

    @PostConstruct
    public void init() {
        ventanaListaRegistros.setControl(this);
    }

    /**
     * Inicia el flujo pidiendo los registros al servicio
     * y mostrándolos en la ventana.
     */
    public void inicia() {
        List<RegistroEmocional> registros = servicioRegistroEmocional.listarRegistros();
        ventanaListaRegistros.muestra(registros);
    }

    public void termina() {
        ventanaListaRegistros.setVisible(false);
    }
}