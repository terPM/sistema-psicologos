package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "psicologo_id")
    private Psicologo psicologo;

    @Column(length = 1000)
    private String mensaje;

    private LocalDateTime fecha;

    private boolean leida = false;

    public Notificacion() {}

    public Notificacion(Psicologo psicologo, String mensaje, LocalDateTime fecha) {
        this.psicologo = psicologo;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.leida = false;
    }

    // getters / setters
    public Long getId() { return id; }
    public Psicologo getPsicologo() { return psicologo; }
    public void setPsicologo(Psicologo psicologo) { this.psicologo = psicologo; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public boolean isLeida() { return leida; }
    public void setLeida(boolean leida) { this.leida = leida; }
}
