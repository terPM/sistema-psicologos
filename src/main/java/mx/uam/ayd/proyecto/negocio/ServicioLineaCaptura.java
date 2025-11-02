package mx.uam.ayd.proyecto.negocio;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import org.springframework.stereotype.Service;


@Service
public class ServicioLineaCaptura {

    /**
     * Genera una cadena numérica aleatoria que simula ser una línea de captura.
     * Esta línea tiene una longitud fija (10 dígitos).
     * * @return Una String de 10 dígitos aleatorios.
     */
    public String generarLineaCaptura() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * Asigna el precio fijo para una cita actualmente de $100.00.
     * * @return El precio de la cita (double).
     */
    public double asignarPrecioCita() {
        return 100.00;         
    }

    /**
     * Obtiene la fecha actual del sistema y la formatea como "día/mes/año".
     * * @return La fecha actual en formato dd/MM/yyyy.
     */
    public String fechaActual() {
        LocalDate fecha = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return fecha.format(formatter);
    }

    /**
     * Registra un pago realizado por un paciente. 
     * Actualmente simula la persistencia mediante una impresión en consola (stub).
     * * @param nombre El nombre del paciente que realiza el pago.
     * @param total El monto total pagado.
     * @param linea La línea de captura utilizada.
     * @param fecha La fecha en que se registró el pago.
     */
    public void registrarPago(String nombre, double total, String linea, String fecha) {
        System.out.println("SERVICIO: Pago registrado y persistido para " + nombre);
    }

}
