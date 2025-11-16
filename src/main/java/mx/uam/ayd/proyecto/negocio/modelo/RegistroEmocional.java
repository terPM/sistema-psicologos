package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class RegistroEmocional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;
    private String emocion;

    @Column(columnDefinition="TEXT")
    private String nota;

    /**
     * Relación: Muchos Registros Emocionales pertenecen a Un Paciente.
     */
    @ManyToOne
    @JoinColumn(name = "paciente_id") // Así se llamará la columna en la BD
    private Paciente paciente;
}