package mx.uam.ayd.proyecto.negocio;

import mx.uam.ayd.proyecto.negocio.modelo.Nota;
import mx.uam.ayd.proyecto.negocio.modelo.Paciente;

import mx.uam.ayd.proyecto.datos.NotaRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio que gestiona la lógica de negocio para las notas de los pacientes.
 */
@Service
public class ServicioNota {

    @Autowired
    private NotaRepository notaRepository;

    /**
     * Guarda una nueva nota en la base de datos.
     *
     * @param paciente el paciente al que pertenece la nota
     * @param texto el contenido de la nota
     * @param fecha la fecha de creación de la nota
     * @return la nota guardada
     */
    public Nota guardarNota(Paciente paciente, String texto, LocalDate fecha) {
        if (paciente == null || texto == null || texto.trim().isEmpty() || fecha == null) {
            throw new IllegalArgumentException("Datos inválidos para registrar la nota");
        }

        Nota nota = new Nota();
        nota.setPaciente(paciente);
        nota.setNota(texto.trim());
        nota.setFecha(fecha);

        return notaRepository.save(nota);
    }

    /**
     * Recupera todas las notas de un paciente.
     */
    public List<Nota> obtenerNotasPorPaciente(Paciente paciente) {
        return notaRepository.findByPaciente(paciente);
    }
}
