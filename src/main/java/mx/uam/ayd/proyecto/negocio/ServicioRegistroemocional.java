package mx.uam.ayd.proyecto.negocio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mx.uam.ayd.proyecto.datos.RegistroEmocionalRepository;
import mx.uam.ayd.proyecto.negocio.modelo.RegistroEmocional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio que encapsula la lógica de negocio para el Registro Emocional.
 */
@Service
public class ServicioRegistroemocional {

    private static final Logger log = LoggerFactory.getLogger(ServicioRegistroemocional.class);

    @Autowired
    private RegistroEmocionalRepository registroEmocionalRepository;

    /**
     * Guarda un nuevo registro emocional.
     *
     * @param emocion La emoción seleccionada (ej. "Feliz", "Triste").
     * @param nota La nota opcional escrita por el usuario.
     * @return El registro guardado.
     * @throws IllegalArgumentException si la emoción es nula o vacía.
     */
    public RegistroEmocional guardarRegistro(String emocion, String nota) {
        if (emocion == null || emocion.trim().isEmpty()) {
            throw new IllegalArgumentException("La emoción no puede estar vacía");
        }

        log.info("Guardando nuevo registro emocional para la emoción: " + emocion);

        RegistroEmocional registro = new RegistroEmocional();
        registro.setFecha(LocalDate.now()); // Fecha actual
        registro.setEmocion(emocion);
        registro.setNota(nota);

         registroEmocionalRepository.save(registro);
         return registro;
    }

    /**
     * Obtiene todos los registros emocionales.
     *
     * @return Una lista de todos los registros.
     */
    public List<RegistroEmocional> listarRegistros() {
        List<RegistroEmocional> registros = new ArrayList<>();
        registroEmocionalRepository.findAll().forEach(registros::add);
        return registros;
    }
}