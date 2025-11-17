package mx.uam.ayd.proyecto.presentacion.principal;

import mx.uam.ayd.proyecto.negocio.modelo.Paciente;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;
import mx.uam.ayd.proyecto.presentacion.menu.ControlMenu;
import mx.uam.ayd.proyecto.presentacion.psicologoPrincipal.ControlPsicologo;
import mx.uam.ayd.proyecto.presentacion.pacientePrincipal.ControlPaciente;
import mx.uam.ayd.proyecto.datos.PsicologoRepository;
import mx.uam.ayd.proyecto.datos.PacienteRepository;
import mx.uam.ayd.proyecto.negocio.ServicioSesion; // Import de HEAD

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ControlPrincipalCentro {

    private static final String USER_ADMIN = "admin";
    private static final String PASS_ADMIN = "admin1234";

    private final VentanaPrincipalCentro ventanaLogin;
    private final ControlMenu controlMenuAdmin;
    private final ControlPsicologo controlPsicologo;
    private final ControlPaciente controlPaciente;

    private final PsicologoRepository psicologoRepository;
    private final PacienteRepository pacienteRepository;
    private final ServicioSesion servicioSesion; // Campo de HEAD

    @Autowired
    public ControlPrincipalCentro(
            VentanaPrincipalCentro ventanaLogin,
            ControlMenu controlMenuAdmin,
            ControlPsicologo controlPsicologo,
            ControlPaciente controlPaciente,
            PsicologoRepository psicologoRepository,
            PacienteRepository pacienteRepository,
            ServicioSesion servicioSesion // Parámetro de HEAD
    ) {
        this.ventanaLogin = ventanaLogin;
        this.controlMenuAdmin = controlMenuAdmin;
        this.controlPsicologo = controlPsicologo;
        this.controlPaciente = controlPaciente;
        this.pacienteRepository = pacienteRepository;
        this.psicologoRepository = psicologoRepository;
        this.servicioSesion = servicioSesion; // Asignación de HEAD
    }

    @PostConstruct
    public void init() {
        ventanaLogin.setControlLoginPrincipal(this);
    }

    public void inicia() {
        ventanaLogin.muestra();
    }

    // Método de HEAD
    public void regresaAlLogin() {
        servicioSesion.limpiarSesion();
        ventanaLogin.limpiarCampos();
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
                    servicioSesion.setUsuarioActual(usuario); // Lógica de HEAD
                    mostrarSistemaPrincipalPsicologo(psicologo); // Lógica de hu-16
                }
                break;

            case "Administrador":
                if (USER_ADMIN.equals(usuario) && PASS_ADMIN.equals(contrasena)) {
                    autenticado = true;
                    servicioSesion.setUsuarioActual(usuario); // Lógica de HEAD
                    mostrarSistemaPrincipalAdministrativo();
                }
                break;

            case "Paciente":
                Paciente paciente = pacienteRepository.findByUsuario(usuario);
                if (paciente != null && paciente.getContrasena().equals(contrasena)) {
                    autenticado = true;
                    servicioSesion.setUsuarioActual(usuario); // Lógica de HEAD
                    mostrarSistemaPrincipalPaciente(paciente); // Lógica de hu-16
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

    // Método de hu-16 (el correcto)
    public void mostrarSistemaPrincipalPaciente(Paciente paciente) {
        ventanaLogin.cerrarLogin();
        controlPaciente.inicia(paciente, this);
    }
}