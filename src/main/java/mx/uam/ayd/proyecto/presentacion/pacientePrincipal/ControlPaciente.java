package mx.uam.ayd.proyecto.presentacion.pacientePrincipal;

import javafx.application.Platform;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.reagendarCita.ControlReagendarCita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Lazy;

<<<<<<< HEAD
import mx.uam.ayd.proyecto.negocio.ServicioAviso;
import mx.uam.ayd.proyecto.negocio.modelo.Aviso;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

=======
import mx.uam.ayd.proyecto.negocio.ServicioCita;
import mx.uam.ayd.proyecto.negocio.modelo.Cita;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
>>>>>>> hu-16-historial-de-pagos
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.RegistroEmocinal.ControlRegistroEmocinal;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.lineaCaptura.ControlLineaCaptura;
import mx.uam.ayd.proyecto.presentacion.principal.ControlPrincipalCentro;
<<<<<<< HEAD
import mx.uam.ayd.proyecto.presentacion.crearCita.ControlCrearCita;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.ListarCitas.ControlListarCitas;
=======
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.HistorialPagos.ControlHistorialPagos;
>>>>>>> hu-16-historial-de-pagos

@Component
public class ControlPaciente {

    @Autowired
    private VentanaPacientePrincipal ventana;

    @Autowired
    private ControlRegistroEmocinal controlRegistroEmocinal;

    @Autowired
<<<<<<< HEAD
    private ControlListaRegistros controlListaRegistros;

    @Autowired
=======
>>>>>>> hu-16-historial-de-pagos
    @Lazy
    private ControlLineaCaptura controlLineaCaptura;
    @Autowired
    private ServicioCita servicioCita;

    @Autowired
<<<<<<< HEAD
    private ControlCrearCita controlCrearCita;

    @Autowired
    private ControlListarCitas controlListarCitas;

    @Autowired
    private ControlReagendarCita controlReagendarCita;

    @Autowired
    private ServicioAviso servicioAviso;

    private ControlPrincipalCentro controlPrincipal;
    private String nombreUsuarioActivo;



    // =============================================================
    //    INICIO DEL PACIENTE
    // =============================================================

    public void inicia(String nombreUsuarioActivo, ControlPrincipalCentro controlPrincipal) {
        this.nombreUsuarioActivo = nombreUsuarioActivo;
        this.controlPrincipal = controlPrincipal;

=======
    private ControlHistorialPagos controlHistorialPagos;

    private ControlPrincipalCentro controlPrincipal;
    private Paciente pacienteSesion;

    public void inicia(Paciente paciente, ControlPrincipalCentro controlPrincipal) {
        this.pacienteSesion = paciente;
        this.controlPrincipal = controlPrincipal;
>>>>>>> hu-16-historial-de-pagos
        ventana.setControlador(this);
        ventana.muestra();
        cargarAvisos();
    }

<<<<<<< HEAD
    private void cargarAvisos() {
        try {
            Aviso ultimoAviso = servicioAviso.obtenerUltimoAviso();
            String textoParaMostrar;

            if (ultimoAviso != null) {

                LocalDate fecha = ultimoAviso.getFecha();
                String contenido = ultimoAviso.getContenido();

                String fechaFormateada = "Publicado el: " + fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                textoParaMostrar = fechaFormateada + "\n\n" + contenido;

            } else {
                textoParaMostrar = "No hay avisos nuevos por el momento.";
            }

            ventana.setAvisos(textoParaMostrar);

        } catch (Exception e) {
            e.printStackTrace();
            ventana.setAvisos("No se pudieron cargar los avisos en este momento.");
        }
    }


    public String getNombreUsuarioActivo() {
        return nombreUsuarioActivo;
    }


    // =============================================================
    //                    ACCIONES DEL MENÚ
    // =============================================================

    public void salir() {
        ventana.oculta();
=======
    public Paciente getPacienteSesion() {
        return pacienteSesion;
    }

    public String getNombreUsuarioActivo() {
        return (pacienteSesion != null) ? pacienteSesion.getUsuario() : null;
    }

    public void salir() {
        ventana.oculta();
        this.pacienteSesion = null;
>>>>>>> hu-16-historial-de-pagos
        if (controlPrincipal != null) {
            controlPrincipal.regresaAlLogin();
        } else {
            Platform.exit();
        }
    }

    public void iniciarRegistroEmocional() {
<<<<<<< HEAD
        controlRegistroEmocinal.inicia();
    }

    public void iniciarListaRegistros() {
        controlListaRegistros.inicia();
    }

    public void iniciarLineaCaptura() {
        if (nombreUsuarioActivo != null) {
            controlLineaCaptura.inicia();
        }
    }

    public void iniciarCrearCita() {
        controlCrearCita.inicia(nombreUsuarioActivo);
    }

    public void iniciarListarCitas() {
        controlListarCitas.inicia(nombreUsuarioActivo);  // ← CORREGIDO
    }


    // =============================================================
    //             *** REAGENDAR CITA (CORRECTO) ***
    // =============================================================

    public void iniciarReagendarCita() {
        if (nombreUsuarioActivo != null) {
            controlReagendarCita.inicia(nombreUsuarioActivo);
        } else {
            System.err.println("Error: nombreUsuarioActivo es null en iniciarReagendarCita()");
        }
    }

}
=======
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
>>>>>>> hu-16-historial-de-pagos
