package mx.uam.ayd.proyecto.presentacion.principal; // Paquete correcto

import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;
import mx.uam.ayd.proyecto.presentacion.menu.ControlMenu;
import mx.uam.ayd.proyecto.presentacion.psicologoPrincipal.ControlPsicologo;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.ControlPaciente;
import mx.uam.ayd.proyecto.datos.PsicologoRepository;
import mx.uam.ayd.proyecto.datos.PacienteRepository;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ControlPrincipalCentro {

    // --- Credenciales de prueba ---
    private static final String USER_ADMIN = "admin";
    private static final String PASS_ADMIN = "admin1234";
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
        ventanaLogin.setControlLoginPrincipal(this);
    }

    public void inicia() {
        ventanaLogin.muestra();
    }

    public void regresaAlLogin() {
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
            if (psicologo != null) { 
                autenticado = true;
                mostrarSistemaPrincipalPsicologo(psicologo);
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

    public void mostrarSistemaPrincipalPsicologo(Psicologo psicologo) { 
        ventanaLogin.cerrarLogin();
        controlPsicologo.inicia(this, psicologo); 
    }

    public void mostrarSistemaPrincipalAdministrativo() {
        ventanaLogin.cerrarLogin();
        controlMenuAdmin.inicia(this);
    }

    public void mostrarSistemaPrincipalPaciente(String nombreDeUsuario) {
        ventanaLogin.cerrarLogin();
        controlPaciente.inicia(nombreDeUsuario, this);
    }
}