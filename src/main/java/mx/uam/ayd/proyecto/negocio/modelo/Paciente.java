package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime; // Importante
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String telefono;
    private String correo;
    private int edad;
    private String usuario;
    private String contrasena;

    // NUEVO CAMPO para la regla de negocio de 72 horas
    private LocalDateTime fechaUltimaActualizacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "psicologo_id")
    private Psicologo psicologo;

    @OneToOne(mappedBy = "paciente", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private HistorialClinico historialClinico;

    @OneToMany(mappedBy = "paciente", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<BateriaClinica> bateriasClinicas;

    @OneToMany(mappedBy = "paciente", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Cita> citas;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RegistroEmocional> registrosEmocionales;

    public Paciente(){
        this.bateriasClinicas = new ArrayList<>();
        this.citas = new ArrayList<>();
        this.registrosEmocionales = new ArrayList<>();
    }
}