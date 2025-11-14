package mx.uam.ayd.proyecto.negocio;
import org.springframework.stereotype.Component;

/**
 * Servicio para gestionar la sesión del usuario.
 * Permite almacenar y recuperar el usuario actualmente activo.
 * 
 */

@Component
public class ServicioSesion {
    private String usuarioActual;

    public void setUsuarioActual(String usuario) {
        this.usuarioActual = usuario;
    }

    public String getUsuarioActual() {
        return usuarioActual;
    }

    public void limpiarSesion() {
        usuarioActual = null;
    }
}
