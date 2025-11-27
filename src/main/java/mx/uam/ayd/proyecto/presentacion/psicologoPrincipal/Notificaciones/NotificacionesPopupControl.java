package mx.uam.ayd.proyecto.presentacion.psicologoPrincipal.Notificaciones;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import mx.uam.ayd.proyecto.negocio.modelo.Notificacion;

import java.util.List;

public class NotificacionesPopupControl {

    @FXML private ListView<String> listaNotificaciones;

    public void cargar(List<Notificacion> notificaciones) {
        listaNotificaciones.getItems().clear();

        if (notificaciones == null || notificaciones.isEmpty()) {
            listaNotificaciones.getItems().add("No hay notificaciones nuevas");
            return;
        }

        for (Notificacion n : notificaciones) {
            listaNotificaciones.getItems().add(
                    "[" + n.getFecha().toString().replace("T", " ") + "] " + n.getMensaje()
            );
        }
    }
}
