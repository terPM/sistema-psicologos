package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDateTime fechaCita;

    @Enumerated(EnumType.STRING)
    private TipoConfirmacionCita estadoCita;

    private String motivo;
    private String detallesAdicionalesPsicologo; // (Viene de la rama HEAD)
    private String detallesAdicionalesPaciente; // (Viene de la rama HEAD)
    private String notaPostSesion;
    private String motivoCancelacion;

    // --- CORRECCIÓN CRÍTICA ---
    // Se cambia de 'double' (primitivo) a 'Double' (objeto)
    // para permitir valores NULL en la base de datos y evitar el crash al arrancar.
    private Double monto;
    // --- FIN DE CORRECCIÓN ---

    private String lineaCaptura;
    private LocalDate fechaVencimiento;


    // Relación con Paciente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    // Relación con Psicologo
    @ManyToOne(fetch = FetchType.EAGER) // Se mantiene EAGER por compatibilidad
    @JoinColumn(name = "psicologo_id")
    private Psicologo psicologo;
}