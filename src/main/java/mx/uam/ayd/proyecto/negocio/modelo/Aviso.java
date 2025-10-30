package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

/**
 * Entidad que representa un aviso o anuncio en el sistema.
 *
 * <p>Contiene el texto del aviso y la fecha en que fue publicado.
 * Está destinado a ser mostrado a los pacientes como información
 * o novedades del centro.</p>
 *
 * <p>Esta entidad está mapeada a la base de datos mediante anotaciones JPA
 * y permite persistir la información de los avisos.</p>
 *
 * @author Tech Solutions (adaptado de Psicologo)
 * @version 1.0
 */
@Entity
@Data
public class Aviso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Contenido textual del aviso.
     * Se mapea como tipo TEXT en la base de datos para permitir
     * contenido de longitud variable y extensa.
     */
    @Column(columnDefinition="TEXT")
    private String contenido;

    /**
     * Fecha en que el aviso fue publicado.
     */
    private LocalDate fecha;

}