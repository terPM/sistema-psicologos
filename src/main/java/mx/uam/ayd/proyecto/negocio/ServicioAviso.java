package mx.uam.ayd.proyecto.negocio;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Asumimos que existen estas clases
import mx.uam.ayd.proyecto.datos.AvisoRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Aviso;

/**
 * Servicio que encapsula la lógica de negocio relacionada con los Avisos.
 *
 * <p>Permite publicar nuevos avisos y recuperar los existentes.</p>
 *
 * <p>Utiliza {@link AvisoRepository} como
 * capa de acceso a datos.</p>
 *
 * @author Tech Solutions (adaptado de ServicioPsicologo)
 * @version 1.0
 */
@Service
public class ServicioAviso {

    private static final Logger log = LoggerFactory.getLogger(ServicioAviso.class);
    
    @Autowired
    private AvisoRepository avisoRepository;

    /**
     * Guarda un nuevo aviso en el sistema.
     *
     * <p>Validaciones aplicadas:
     * <ul>
     * <li>El contenido del aviso no puede ser nulo ni vacío.</li>
     * </ul>
     * </p>
     *
     * @param contenido el texto del aviso a publicar.
     * @return el aviso guardado.
     * @throws IllegalArgumentException si el contenido es inválido.
     */
    public Aviso guardarAviso(String contenido) {
        if(contenido == null || contenido.trim().isEmpty()) {
            throw new IllegalArgumentException("El contenido del aviso no puede ser nulo o vacío");
        }

        log.info("Guardando nuevo aviso...");

        //Creamos el nuevo aviso
        Aviso aviso = new Aviso();
        aviso.setContenido(contenido);
        aviso.setFecha(LocalDate.now()); // Asignamos la fecha actual

        avisoRepository.save(aviso);

        return aviso;
    }

    /**
     * Obtiene el aviso más reciente publicado.
     * * <p>Este método requiere que AvisoRepository tenga un método como:
     * <code>public Aviso findTopByOrderByFechaDesc();</code></p>
     *
     * @return el último aviso publicado, o null si no hay ninguno.
     */
    public Aviso obtenerUltimoAviso() {
        
        return avisoRepository.findTopByOrderByIdDesc();
    }
    
    /**
     * Lista todos los avisos registrados.
     *
     * @return una lista con todos los avisos; si no existen registros, la lista estará vacía.
     */
    public List<Aviso> listarAvisos() {
        List<Aviso> avisos = new ArrayList<>();
        avisoRepository.findAll().forEach(avisos::add);
        return avisos;
    }
}