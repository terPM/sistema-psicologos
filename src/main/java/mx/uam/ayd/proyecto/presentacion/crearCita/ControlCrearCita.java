package mx.uam.ayd.proyecto.presentacion.crearCita;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.negocio.ServicioCita;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;
import mx.uam.ayd.proyecto.negocio.ServicioPaciente;

@Component
public class ControlCrearCita {

    @Autowired
    private ServicioPaciente servicioPaciente;

    @Autowired
    private VentanaCrearCita ventanaCrearCita;

    @Autowired
    private ServicioCita servicioCita;

    String nombreUsuarioActivo;

    public void inicia(String nombreUsuarioActivo) {
        this.nombreUsuarioActivo = nombreUsuarioActivo;

        Paciente pacienteActivo = servicioPaciente.obtenerPacientePorUsuario(nombreUsuarioActivo);
        Psicologo psicologoAsignado = pacienteActivo.getPsicologo();

        ventanaCrearCita.setControlCrearCita(this);
        ventanaCrearCita.muestra();
        ventanaCrearCita.setDatosPacienteYPsicologo(
                pacienteActivo.getId(),
                pacienteActivo.getNombre(),
                psicologoAsignado.getId(),
                psicologoAsignado.getNombre()
        );
    }

    public void onFechaSeleccionada(LocalDate fecha) {
        if (fecha == null) {
            ventanaCrearCita.setHorarios(new ArrayList<>());
            return;
        }

        DayOfWeek dow = fecha.getDayOfWeek();
        int inicio;
        int fin;

        if (dow == DayOfWeek.SATURDAY) {
            inicio = 9;
            fin = 14;
        } else if (dow == DayOfWeek.SUNDAY) {
            ventanaCrearCita.setHorarios(new ArrayList<>());
            return;
        } else {
            inicio = 8;
            fin = 17;
        }

        List<String> horarios = new ArrayList<>();
        for (int h = inicio; h <= fin; h++) {
            horarios.add(String.format("%02d:00", h));
        }

        ventanaCrearCita.setHorarios(horarios);
    }

    public void guardarCita(LocalDate fechaSeleccionada, String horarioSeleccionado) {
        if (fechaSeleccionada == null) {
            ventanaCrearCita.muestraDialogoError("Error", "Debe seleccionar una fecha");
            return;
        }

        if (horarioSeleccionado == null || horarioSeleccionado.isBlank()) {
            ventanaCrearCita.muestraDialogoError("Error", "Debe seleccionar un horario");
            return;
        }

        if (fechaSeleccionada.isBefore(LocalDate.now())) {
            ventanaCrearCita.muestraDialogoError("Fecha inválida", "No se pueden agendar citas en fechas anteriores a hoy.");
            return;
        }

        try {
            int hora = Integer.parseInt(horarioSeleccionado.split(":")[0]);
            LocalDateTime fechaHora = fechaSeleccionada.atTime(hora, 0);

            Paciente paciente = servicioPaciente.obtenerPacientePorUsuario(nombreUsuarioActivo);

            String motivo = "Consulta programada";

            servicioCita.crearCita(paciente, fechaHora, motivo);

            ventanaCrearCita.muestraDialogoExito("Cita creada", "La cita se creó correctamente.");
            ventanaCrearCita.cerrar();

        } catch (NumberFormatException e) {
            ventanaCrearCita.muestraDialogoError("Error de formato", "El formato del horario no es válido");
        } catch (IllegalArgumentException e) {
            ventanaCrearCita.muestraDialogoError("Error al crear cita", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            ventanaCrearCita.muestraDialogoError("Error desconocido", e.getMessage());
        }
    }
}
