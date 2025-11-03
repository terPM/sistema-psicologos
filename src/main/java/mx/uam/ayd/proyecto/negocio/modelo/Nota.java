package mx.uam.ayd.proyecto.negocio.modelo;

import java.time.LocalDate;;
import jakarta.persistence.*;
import lombok.Data;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Entidad que representa una nota escrita por el psicólogo después de cada sesión con un paciente.
 *
 * Cada nota está asociada a un paciente y contiene la fecha y el texto escrito manualmente.
 */
@Entity
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false, length = 1000)
    private String nota;

    public Nota() {
    }

    public Nota(Paciente paciente, LocalDate fecha, String nota) {
        this.paciente = paciente;
        this.fecha = fecha;
        this.nota = nota;
    }

    // --- Getters y Setters ---
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    @Override
    public String toString() {
        return "Nota{" +
                "id=" + id +
                ", paciente=" + paciente +
                ", fecha=" + fecha +
                ", nota='" + nota + '\'' +
                '}';
    }
}

