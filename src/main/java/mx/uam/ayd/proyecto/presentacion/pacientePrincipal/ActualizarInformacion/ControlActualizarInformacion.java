package mx.uam.ayd.proyecto.presentacion.pacientePrincipal.ActualizarInformacion;

import javafx.scene.control.Alert;
import mx.uam.ayd.proyecto.negocio.ServicioPaciente;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.ControlPaciente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Control que gestiona el flujo de actualización de datos personales del paciente.
 * Se encarga de la comunicación entre la vista y el servicio de negocio.
 */
@Component
public class ControlActualizarInformacion {

    @Autowired
    private VentanaActualizarInformacion ventana;

    @Autowired
    private ServicioPaciente servicioPaciente;

    private ControlPaciente controlPaciente;
    private Paciente pacienteActual;

    /**
     * Inicializa el control, carga los datos frescos del paciente y muestra la ventana.
     * * @param paciente El paciente que ha iniciado sesión.
     * @param controlPaciente Referencia al control principal para actualizar la sesión si es necesario.
     */
    public void inicia(Paciente paciente, ControlPaciente controlPaciente) {
        this.controlPaciente = controlPaciente;
        // Recargar paciente de la BD para asegurar datos frescos
        this.pacienteActual = servicioPaciente.obtenerPacientePorUsuario(paciente.getUsuario());

        if (pacienteActual != null) {
            ventana.setControlador(this);
            ventana.muestra(pacienteActual);
        } else {
            System.err.println("Error: Paciente no encontrado.");
        }
    }

    /**
     * Válida las reglas de negocio (tiempo, campos vacíos, contraseñas) y solicita
     * al servicio la actualización de los datos del paciente.
     * * @param nuevoUsuario El nuevo nombre de usuario.
     * @param edadStr La edad en formato texto.
     * @param telefono El número de teléfono.
     * @param correo El correo electrónico.
     * @param passActual La contraseña actual para verificación.
     * @param passNueva La nueva contraseña (opcional).
     * @param passConfirmar La confirmación de la nueva contraseña.
     */
    public void actualizarDatos(String nuevoUsuario, String edadStr, String telefono, String correo,
                                String passActual, String passNueva, String passConfirmar) {

        // 1. REGLA: Validar 72 horas
        if (pacienteActual.getFechaUltimaActualizacion() != null) {
            long horasDiferencia = ChronoUnit.HOURS.between(pacienteActual.getFechaUltimaActualizacion(), LocalDateTime.now());
            if (horasDiferencia < 72) {
                long horasRestantes = 72 - horasDiferencia;
                ventana.muestraMensaje("Restricción de Tiempo",
                        "No puede actualizar su información todavía.\nDebe esperar " + horasRestantes + " horas más.",
                        Alert.AlertType.WARNING);
                return;
            }
        }

        // 2. Validar campos vacíos
        if (nuevoUsuario.isEmpty() || edadStr.isEmpty() || telefono.isEmpty() || correo.isEmpty()) {
            ventana.muestraMensaje("Error", "Todos los campos de información son obligatorios.", Alert.AlertType.WARNING);
            return;
        }

        // 3. Validar contraseña actual (Obligatoria para seguridad)
        if (passActual.isEmpty()) {
            ventana.muestraMensaje("Seguridad", "Ingrese su contraseña actual para confirmar los cambios.", Alert.AlertType.WARNING);
            return;
        }

        if (!pacienteActual.getContrasena().equals(passActual)) {
            ventana.muestraMensaje("Error", "La contraseña actual es incorrecta.", Alert.AlertType.ERROR);
            return;
        }

        // 4. Cambio de contraseña opcional
        boolean cambiarPass = !passNueva.isEmpty();
        if (cambiarPass) {
            if (!passNueva.equals(passConfirmar)) {
                ventana.muestraMensaje("Error", "Las nuevas contraseñas no coinciden.", Alert.AlertType.ERROR);
                return;
            }
            if (passNueva.length() < 4) {
                ventana.muestraMensaje("Error", "La contraseña es muy corta (min 4 caracteres).", Alert.AlertType.WARNING);
                return;
            }
        }

        // 5. Guardar
        try {
            int edad = Integer.parseInt(edadStr);

            pacienteActual.setUsuario(nuevoUsuario);
            pacienteActual.setEdad(edad);
            pacienteActual.setTelefono(telefono);
            pacienteActual.setCorreo(correo);

            if (cambiarPass) {
                pacienteActual.setContrasena(passNueva);
            }

            pacienteActual.setFechaUltimaActualizacion(LocalDateTime.now());

            // Persistir en BD
            servicioPaciente.actualizarPaciente(pacienteActual);

            // *** IMPORTANTE: ACTUALIZAR LA SESIÓN EN EL MENÚ PRINCIPAL ***
            if (controlPaciente != null) {
                controlPaciente.actualizarSesion(pacienteActual);
            }

            ventana.muestraMensaje("Éxito", "Información actualizada correctamente.", Alert.AlertType.INFORMATION);

            // Refrescar ventana actual
            ventana.muestra(pacienteActual);

        } catch (NumberFormatException e) {
            ventana.muestraMensaje("Error", "La edad debe ser un número válido.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            ventana.muestraMensaje("Error del Sistema", "Fallo en la actualización: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Cierra la ventana y termina el flujo de actualización.
     */
    public void regresa() {
        ventana.oculta();
    }
}