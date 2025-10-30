package mx.uam.ayd.proyecto.presentacion.listaAvisos;

import jakarta.annotation.PostConstruct;
import mx.uam.ayd.proyecto.negocio.ServicioAviso;
import mx.uam.ayd.proyecto.negocio.modelo.Aviso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Controlador para la funcionalidad de listar todos los avisos existentes.
 *
 * <p>Esta clase actúa como intermediario entre la vista {@link VentanaListaAvisos}
 * y el servicio de negocio {@link ServicioAviso}.</p>
 *
 * <p>Sus responsabilidades principales incluyen:</p>
 * <ul>
 * <li>Solicitar al servicio la lista completa de avisos.</li>
 * <li>Pasar la lista de avisos a la ventana para que los muestre.</li>
 * <li>Mostrar y cerrar la ventana de la lista.</li>
 * </ul>
 */
@Component
public class ControlListaAvisos {

    @Autowired
    private VentanaListaAvisos ventanaListaAvisos;

    @Autowired
    private ServicioAviso servicioAviso;

    /**
     * Inicializa la conexión entre esta clase de control y la ventana asociada.
     * Este método se ejecuta automáticamente después de la construcción del bean.
     */
    @PostConstruct
    public void init() {
        ventanaListaAvisos.setControl(this);
    }

    /**
     * Inicia el flujo de listar avisos.
     * <p>Recupera todos los avisos del servicio y se los pasa a la ventana
     * para que los muestre.</p>
     */
    public void inicia() {
        // 1. Pedimos los datos al servicio de negocio
        List<Aviso> avisos = servicioAviso.listarAvisos();

        // 2. Pasamos los datos a la ventana para que los "pinte"
        ventanaListaAvisos.muestra(avisos);
    }

    /**
     * Finaliza el flujo actual ocultando la ventana de la lista de avisos.
     */
    public void termina() {
        ventanaListaAvisos.setVisible(false);
    }
}