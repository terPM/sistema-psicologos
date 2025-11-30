package mx.uam.ayd.proyecto.negocio.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne; 
import jakarta.persistence.JoinColumn; 
import jakarta.persistence.FetchType; 

import java.time.LocalDateTime;

/**
 * Representa la respuesta de un paciente a la Encuesta de Evaluación del Servicio.
 * Esta clase es una Entidad JPA para la persistencia en la base de datos.
 */
@Entity
public class EncuestaSatisfaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id; 

    @Column(nullable = false)
    private LocalDateTime fechaHora; // Fecha y hora en que se completó la encuesta

    // RELACIÓN CON PACIENTE 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false) 
    private Paciente paciente; 

    private int q1Empatia; 
    private int q2Confianza;
    private int q3Respeto;
    private int q4Confidencialidad;
    private int q5Herramientas;
    private int q6Satisfaccion;
    private int q7Recomendacion;


    @Column(length = 500)
    private String q8Mejora;
    
    @Column(length = 500)
    private String q9Comentarios;

    public EncuestaSatisfaccion() {
        this.fechaHora = LocalDateTime.now(); 
    }
    
    // Getters y Setters para el ID (clave primaria)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    // Getters y Setters para fechaHora
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
    
    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    // Getters y Setters para Q1-Q7 (Campos numéricos)
    public int getQ1Empatia() {
        return q1Empatia;
    }

    public void setQ1Empatia(int q1Empatia) {
        this.q1Empatia = q1Empatia;
    }
    
    public int getQ2Confianza() {
        return q2Confianza;
    }

    public void setQ2Confianza(int q2Confianza) {
        this.q2Confianza = q2Confianza;
    }

    public int getQ3Respeto() {
        return q3Respeto;
    }

    public void setQ3Respeto(int q3Respeto) {
        this.q3Respeto = q3Respeto;
    }

    public int getQ4Confidencialidad() {
        return q4Confidencialidad;
    }

    public void setQ4Confidencialidad(int q4Confidencialidad) {
        this.q4Confidencialidad = q4Confidencialidad;
    }

    public int getQ5Herramientas() {
        return q5Herramientas;
    }

    public void setQ5Herramientas(int q5Herramientas) {
        this.q5Herramientas = q5Herramientas;
    }

    public int getQ6Satisfaccion() {
        return q6Satisfaccion;
    }

    public void setQ6Satisfaccion(int q6Satisfaccion) {
        this.q6Satisfaccion = q6Satisfaccion;
    }

    public int getQ7Recomendacion() {
        return q7Recomendacion;
    }

    public void setQ7Recomendacion(int q7Recomendacion) {
        this.q7Recomendacion = q7Recomendacion;
    }

    // Getters y Setters para Q8-Q9 (Campos de texto)
    public String getQ8Mejora() {
        return q8Mejora;
    }

    public void setQ8Mejora(String q8Mejora) {
        this.q8Mejora = q8Mejora;
    }

    public String getQ9Comentarios() {
        return q9Comentarios;
    }

    public void setQ9Comentarios(String q9Comentarios) {
        this.q9Comentarios = q9Comentarios;
    }
}
