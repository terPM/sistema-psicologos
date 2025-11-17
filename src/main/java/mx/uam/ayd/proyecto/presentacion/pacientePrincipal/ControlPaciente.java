package mx.uam.ayd.proyecto.presentacion.pacientePrincipal;

import javafx.application.Platform;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.reagendarCita.ControlReagendarCita;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.reagendarCita.VentanaReagendarCita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Lazy;

import mx.uam.ayd.proyecto.negocio.ServicioAviso;
import mx.uam.ayd.proyecto.negocio.modelo.Aviso;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.RegistroEmocinal.ControlRegistroEmocinal;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.lineaCaptura.ControlLineaCaptura;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.ListaRegistros.ControlListaRegistros;
import mx.uam.ayd.proyecto.presentacion.principal.ControlPrincipalCentro;
import mx.uam.ayd.proyecto.presentacion.crearCita.ControlCrearCita;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.ListarCitas.ControlListarCitas;

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
    @Autowired
    private ControlCrearCita controlCrearCita;
    @Autowired
    private ControlListarCitas controlListarCitas;
    private ControlReagendarCita controlReagendarCita;

    @Autowired
    private ServicioAviso servicioAviso;

    private ControlPrincipalCentro controlPrincipal;    
    private String nombreUsuarioActivo;

    /**
     * Inicia el flujo principal del paciente, 
     * recibiendo y guardando el nombre de usuario de la sesión.
     * @param nombreUsuarioActivo El nombre de usuario que ingresó en el login.
     * @param controlPrincipal La referencia al controlador principal para la salida.
     */
    public void inicia(String nombreUsuarioActivo, ControlPrincipalCentro controlPrincipal) { 
        this.nombreUsuarioActivo = nombreUsuarioActivo;
        this.controlPrincipal = controlPrincipal; 
        ventana.setControlador(this);
        ventana.muestra();
        cargarAvisos();
    }

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

    /**
     * Devuelve el nombre del usuario activo (obtenido del login).
     */
    public String getNombreUsuarioActivo() { 
        return nombreUsuarioActivo;
    }

    /**
     * Cierra la aplicación
     */
    public void salir() {
        ventana.oculta(); // Oculta la ventana actual del paciente
        if (controlPrincipal != null) {
            controlPrincipal.regresaAlLogin(); // Llama al método de ControlPrincipalCentro
        } else {
            Platform.exit(); // Fallback por si la referencia es nula
        }
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
     */
    public void iniciarLineaCaptura() { 
        if (nombreUsuarioActivo != null) {
            controlLineaCaptura.inicia(); 
        }
    }
    /**
     * Inicia el sub-flujo de Crear Cita.
     */
    public void iniciarCrearCita(){
            controlCrearCita.inicia(nombreUsuarioActivo);
    }

    public void iniciarListarCitas() {
        controlListarCitas.inicia();
    }


    public void iniciarReagendarCita() {
        controlReagendarCita.inicia();
    }




}