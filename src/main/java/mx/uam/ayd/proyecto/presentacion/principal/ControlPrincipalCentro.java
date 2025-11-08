package mx.uam.ayd.proyecto.presentacion.principal; // Paquete correcto

import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;
import mx.uam.ayd.proyecto.presentacion.menu.ControlMenu;
import mx.uam.ayd.proyecto.presentacion.psicologoPrincipal.ControlPsicologo;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.ControlPaciente;
import mx.uam.ayd.proyecto.datos.*;

import jakarta.annotation.PostConstruct;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ControlPrincipalCentro {

    // --- Credenciales de prueba ---
    private static final String USER_PSICOLOGO = "psicologo";
    private static final String PASS_PSICOLOGO = "psi1234";
    private static final String USER_ADMIN = "admin";
    private static final String PASS_ADMIN = "admin1234";
    private static final String USER_PACIENTE = "paciente";
    private static final String PASS_PACIENTE = "pa1234";
    // ----------------------------

    // Usa la clase de Ventana renombrada
    private final VentanaPrincipalCentro ventanaLogin;

    // ControlMenu (el menú completo) ahora es solo para el Administrador
    private final ControlMenu controlMenuAdmin;

    // Controladores de flujos únicos
    private final ControlPsicologo controlPsicologo;
    private final ControlPaciente controlPaciente;

    private final PsicologoRepository psicologoRepository;
    private final PacienteRepository pacienteRepository;

    @Autowired
    public ControlPrincipalCentro(
            VentanaPrincipalCentro ventanaLogin, // Constructor usa clase renombrada
            ControlMenu controlMenuAdmin,
            ControlPsicologo controlPsicologo,
            ControlPaciente controlPaciente,
            PsicologoRepository psicologoRepository,
            PacienteRepository pacienteRepository
    ) {
        this.ventanaLogin = ventanaLogin;
        this.controlMenuAdmin = controlMenuAdmin;
        this.controlPsicologo = controlPsicologo;
        this.controlPaciente = controlPaciente;
        this.pacienteRepository = pacienteRepository;
        this.psicologoRepository = psicologoRepository;
    }

    @PostConstruct
    public void init() {
        // CORRECCIÓN: Llama al método renombrado en la ventana
        ventanaLogin.setControlLoginPrincipal(this);
    }

    public void inicia() {
        ventanaLogin.muestra();
    }

    public void autenticar(String rol, String usuario, String contrasena) {

        if (usuario == null || usuario.trim().isEmpty() ||
                contrasena == null || contrasena.trim().isEmpty() ||
                rol == null || rol.trim().isEmpty()) {
            ventanaLogin.mostrarError("Por favor ingrese usuario, contraseña y seleccione un rol.");
            return;
        }

        boolean autenticado = false;

        switch (rol) {
            case "Psicólogo":
                Psicologo psicologo = psicologoRepository.findByUsuarioAndContrasena(usuario, contrasena);
                if (psicologo.getUsuario().equals(usuario) && psicologo.getContrasena().equals(contrasena)) {
                    autenticado = true;
                    mostrarSistemaPrincipalPsicologo();
                }
                break;
            case "Administrador":
                if (USER_ADMIN.equals(usuario) && PASS_ADMIN.equals(contrasena)) {
                    autenticado = true;
                    mostrarSistemaPrincipalAdministrativo();
                }
                break;
            case "Paciente":
                Paciente paciente = pacienteRepository.findByUsuario(usuario);
                if (paciente != null && paciente.getContrasena().equals(contrasena)) {
                    autenticado = true;
                    mostrarSistemaPrincipalPaciente(usuario);
                }
                break;
            default:
                ventanaLogin.mostrarError("Rol seleccionado no válido.");
                return;
        }

        if (!autenticado) {
            ventanaLogin.mostrarError("Credenciales incorrectas para el rol seleccionado.");
        }
    }

    public void mostrarSistemaPrincipalPsicologo() {
        ventanaLogin.cerrarLogin();
        controlPsicologo.inicia();
    }

    public void mostrarSistemaPrincipalAdministrativo() {
        ventanaLogin.cerrarLogin();
        // El Administrador es el ÚNICO que ve el menú completo (ControlMenu)
        controlMenuAdmin.inicia();
    }

    public void mostrarSistemaPrincipalPaciente(String nombreDeUsuario) {
        ventanaLogin.cerrarLogin();
        controlPaciente.inicia(nombreDeUsuario);
    }
}