package mx.uam.ayd.proyecto.presentacion.crearCita;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.datos.PacienteRepository;
import mx.uam.ayd.proyecto.negocio.ServicioCita;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;

@Component
public class ControlCrearCita {

	@Autowired
	private VentanaCrearCita ventanaCrearCita;

	@Autowired
	private PacienteRepository pacienteRepository;

	@Autowired
	private ServicioCita servicioCita;

	/**
	 * Inicia la ventana para crear citas. Carga los pacientes disponibles.
	 */
	public void inicia() {
		ventanaCrearCita.setControlCrearCita(this);
        ventanaCrearCita.muestra();
		// Cargar pacientes desde el repositorio
		List<Paciente> pacientes = new ArrayList<>();
		pacienteRepository.findAll().forEach(pacientes::add);
		ventanaCrearCita.setPacientes(pacientes);
		
	}

	/**
	 * Calcula y devuelve los horarios disponibles según el día seleccionado.
	 * L - V : 8:00 - 17:00
	 * Sábados: 9:00 - 14:00
	 */
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
			// Domingo: no hay horarios
			ventanaCrearCita.setHorarios(new ArrayList<>());
			return;
		} else {
			// Lunes a Viernes
			inicio = 8;
			fin = 17;
		}

		List<String> horarios = new ArrayList<>();
		for (int h = inicio; h <= fin; h++) {
			horarios.add(String.format("%02d:00", h));
		}

		ventanaCrearCita.setHorarios(horarios);
	}

	/**
	 * Guarda la cita usando el servicio. Convierte la fecha + hora a LocalDateTime.
	 */
	public void guardarCita(Paciente pacienteSeleccionado, String psicologoNombre, LocalDate fechaSeleccionada, String horarioSeleccionado) {
		if (pacienteSeleccionado == null) {
			ventanaCrearCita.muestraDialogoError("Error", "Debe seleccionar un paciente");
			return;
		}
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

			// Usamos el servicio para crear la cita.
			String motivo = (psicologoNombre == null) ? "Cita agendada" : "Cita con: " + psicologoNombre;

			servicioCita.crearCita(pacienteSeleccionado, fechaHora, motivo);

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
