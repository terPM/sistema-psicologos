package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

/**
 * Entidad que representa un registro emocional de un paciente.
 *
 * Contiene la emoción seleccionada, una nota opcional y la fecha del registro.
 */
@Entity
@Data
public class RegistroEmocional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;
    private String emocion;

    /**
     * Nota opcional del paciente.
     * Se mapea como tipo TEXT para permitir contenido extenso.
     */
    @Column(columnDefinition="TEXT")
    private String nota;

}