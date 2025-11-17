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
    private String notaPostSesion;
    private String motivoCancelacion;

    // Campos para almacenar la información de pago generada
    private String lineaCaptura;
    private double monto;
    private LocalDate fechaVencimiento;


    // Relación con Paciente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    // Relación con Psicologo
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "psicologo_id")
    private Psicologo psicologo;
}