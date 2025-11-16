package mx.uam.ayd.proyecto.presentacion.pacientePrincipal.reagendarCita;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.datos.CitaRepository;
import mx.uam.ayd.proyecto.negocio.ServicioCita;
import mx.uam.ayd.proyecto.negocio.ServicioPsicologo;
import mx.uam.ayd.proyecto.negocio.modelo.Cita;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;

/**
 * Controlador del flujo de reagendar cita.
 */
@Component
public class ControlReagendarCita {

    @Autowired
    private VentanaReagendarCita ventana;

    @Autowired
    private ServicioPsicologo servicioPsicologo;

    @Autowired
    private ServicioCita servicioCita;

    @Autowired
    private CitaRepository citaRepository;

    /**
     * Inicia el flujo: carga psicólogos, horas y muestra ventana.
     */
    public void inicia() {
        ventana.setControl(this);
        cargarDatosIniciales();
        ventana.muestra();
    }

    /**
     * Cargar psicólogos reales + horas disponibles.
     */
    public void cargarDatosIniciales() {

        // cargar psicólogos reales
        try {
            List<Psicologo> psicologos = servicioPsicologo.listarPsicologos();
            ventana.setPsicologos(psicologos);
        } catch (Exception e) {
            ventana.mostrarMensaje("Error cargando psicólogos: " + e.getMessage());
        }

        // cargar horas
        List<String> horas = new ArrayList<>();
        for (int h = 8; h <= 12; h++) horas.add(String.format("%02d:00", h));
        for (int h = 15; h <= 17; h++) horas.add(String.format("%02d:00", h));
        ventana.setHoras(horas);
    }

    /**
     * Cargar datos de una cita por ID
     */
    public void cargarCita(String idTexto) {
        if (idTexto == null || idTexto.isBlank()) {
            ventana.mostrarMensaje("Ingrese ID válido");
            return;
        }

        try {
            int id = Integer.parseInt(idTexto);

            // AHORA SE USA EL MÉTODO FETCH COMPLETO
            Cita cita = citaRepository.findByIdConRelaciones(id);

            if (cita == null) {
                ventana.mostrarMensaje("Cita no encontrada");
                return;
            }

            // fecha
            if (cita.getFechaCita() != null) {
                ventana.setFecha(cita.getFechaCita().toLocalDate());
                String hora = String.format("%02d:00", cita.getFechaCita().getHour());
                ventana.setHora(hora);
            }

            // psicólogo
            Psicologo psicologo = cita.getPsicologo();
            if (psicologo != null) {
                List<Psicologo> lista = ventana.getPsicologos();
                var match = lista.stream().filter(p -> p.getId() == psicologo.getId()).findFirst();

                if (match.isPresent())
                    ventana.selectPsicologo(match.get());
                else
                    ventana.addAndSelectPsicologo(psicologo);
            }

            ventana.mostrarMensaje("Cita cargada. Ajusta los datos y guarda.");

        } catch (NumberFormatException e) {
            ventana.mostrarMensaje("ID inválido");
        } catch (Exception e) {
            ventana.mostrarMensaje("Error al cargar cita: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Guarda la re-agenda
     */
    public void guardar(String idTexto, LocalDate fecha, String hora, Psicologo psicologo) {

        if (idTexto == null || idTexto.isBlank()) {
            ventana.mostrarMensaje("Ingrese ID");
            return;
        }
        if (fecha == null) {
            ventana.mostrarMensaje("Seleccione fecha");
            return;
        }
        if (hora == null || hora.isBlank()) {
            ventana.mostrarMensaje("Seleccione hora");
            return;
        }
        if (psicologo == null) {
            ventana.mostrarMensaje("Seleccione un psicólogo");
            return;
        }

        try {
            int id = Integer.parseInt(idTexto);
            servicioCita.reagendarCita(id, fecha, hora, psicologo);
            ventana.mostrarMensaje("Cita reagendada con éxito");
            ventana.cerrar();

        } catch (Exception e) {
            ventana.mostrarMensaje("Error al reagendar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void cancelar() {
        ventana.cerrar();
    }
}

