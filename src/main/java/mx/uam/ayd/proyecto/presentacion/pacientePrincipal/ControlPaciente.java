package mx.uam.ayd.proyecto.presentacion.pacientePrincipal;

import javafx.application.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Lazy;

import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.RegistroEmocinal.ControlRegistroEmocinal;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.lineaCaptura.ControlLineaCaptura;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.ListaRegistros.ControlListaRegistros;

@Component
public class ControlPaciente {

    @Autowired
    private VentanaPacientePrincipal ventana;

    @Autowired
    private ControlRegistroEmocinal controlRegistroEmocinal;

    @Autowired
    private ControlListaRegistros controlListaRegistros;
    
    @Autowired
    @Lazy
    private ControlLineaCaptura controlLineaCaptura;

    private String nombreUsuarioActivo;

    /**
     * ❗ MODIFICACIÓN CRÍTICA: Inicia el flujo principal del paciente, 
     * ❗ recibiendo y guardando el nombre de usuario de la sesión.
     * @param nombreUsuarioActivo El nombre de usuario que ingresó en el login.
     */
    public void inicia(String nombreUsuarioActivo) { 
        this.nombreUsuarioActivo = nombreUsuarioActivo; // ❗ GUARDA LA SESIÓN
        ventana.setControlador(this);
        ventana.muestra();
    }

    /**
     * Devuelve el nombre del usuario activo (obtenido del login).
     */
    public String getNombreUsuarioActivo() { // <-- 4. MÉTODO PARA RECUPERAR EL NOMBRE
        return nombreUsuarioActivo;
    }

    /**
     * Cierra la aplicación
     */
    public void salir() {
        Platform.exit();
    }

    /**
     * Inicia el sub-flujo de Registro Emocional
     */
    public void iniciarRegistroEmocional() {
        controlRegistroEmocinal.inicia();
    }

    /**
     * Inicia el sub-flujo de Lista de Registros
     */
    public void iniciarListaRegistros() {
        controlListaRegistros.inicia();
    }

    /**
     * Inicia el sub-flujo de Generar Línea de Captura.
     * ❗ Se mantiene una sola implementación.
     */
    public void iniciarLineaCaptura() { 
        if (nombreUsuarioActivo != null) {
            controlLineaCaptura.inicia(); 
        }
    }
}