package mx.uam.ayd.proyecto.presentacion.publicarAviso;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import mx.uam.ayd.proyecto.presentacion.menu.ControlMenu;

// Asumimos que tienes un servicio para manejar la lógica de negocio de los avisos
// Deberás crear esta clase si aún no existe.
import mx.uam.ayd.proyecto.negocio.ServicioAviso; 

/**
 * Controlador para la funcionalidad de publicar un nuevo aviso.
 * * Esta clase actúa como intermediario entre la vista {@link VentanaPublicarAviso} 
 * y el servicio de negocio {@link ServicioAviso}.
 * Sus responsabilidades principales incluyen:
 * <ul>
 * <li>Inicializar la conexión con la ventana correspondiente.</li>
 * <li>Mostrar la ventana para que el administrador escriba el aviso.</li>
 * <li>Recibir el texto del aviso y solicitar al servicio que lo guarde.</li>
 * <li>Cerrar la ventana de publicación.</li>
 */
@Component
public class ControlPublicarAviso {

    private final VentanaPublicarAviso ventanaPublicarAviso;

    // Inyectamos el servicio que maneja la lógica de negocio de los avisos
    @Autowired
    private ServicioAviso servicioAviso;
    
    private ControlMenu controlMenu;
    
    @Autowired
    public ControlPublicarAviso(VentanaPublicarAviso ventanaPublicarAviso){
        this.ventanaPublicarAviso = ventanaPublicarAviso;
    }

    /**
     * Inicializa la conexión entre esta clase de control y la ventana asociada.
     * Este método se ejecuta automáticamente después de la construcción del bean.
     */
    @PostConstruct
    public void init(){
        ventanaPublicarAviso.setControl(this);
    }

    /**
     * Inicia el flujo de publicar aviso.
     * <p>Muestra la ventana para que el administrador ingrese el nuevo aviso.</p>
     */
    public void inicia(ControlMenu controlMenu){ 
        this.controlMenu = controlMenu; 
        ventanaPublicarAviso.muestra();
    }

    /**
     * Finaliza el flujo actual ocultando la ventana de publicar aviso.
     */
    public void termina() {
        ventanaPublicarAviso.setVisible(false);
        this.controlMenu = null;
    }

    /**
     * Recibe el contenido del aviso desde la ventana y lo manda al servicio
     * de negocio para ser guardado.
     * * @param contenido El texto del aviso a publicar.
     */
    public void publicarAviso(String contenido) {
        if (contenido == null || contenido.trim().isEmpty()) {
            ventanaPublicarAviso.muestraError("El contenido del aviso no puede estar vacío.");
            return;
        }

        try {
            // Llamamos al servicio para que guarde el aviso
            servicioAviso.guardarAviso(contenido);

            if (this.controlMenu != null) {
                this.controlMenu.actualizarDisplayAviso();
            }
            
            // Si se guarda con éxito, cerramos la ventana
            termina();
            
        } catch (Exception e) {
            // Manejo de cualquier error que ocurra al guardar
            ventanaPublicarAviso.muestraError("Error al guardar el aviso: " + e.getMessage());
        }
    }
}