package mx.uam.ayd.proyecto.presentacion.psicologoPrincipal.ListaRegistros;

import jakarta.annotation.PostConstruct;
import mx.uam.ayd.proyecto.negocio.ServicioRegistroemocional;
import mx.uam.ayd.proyecto.negocio.modelo.RegistroEmocional;
import mx.uam.ayd.proyecto.negocio.modelo.Psicologo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ControlListaRegistros {

    @Autowired
    private VentanaListaRegistros ventanaListaRegistros;

    @Autowired
    private ServicioRegistroemocional servicioRegistroemocional;

    @PostConstruct
    public void init() {
        ventanaListaRegistros.setControl(this);
    }

    /**
     * Inicia el flujo pidiendo los registros del psicólogo
     */
    public void inicia(Psicologo psicologo) {
        List<RegistroEmocional> registros = servicioRegistroemocional.listarRegistrosPorPsicologo(psicologo);
        ventanaListaRegistros.muestra(registros);
    }

    public void termina() {
        ventanaListaRegistros.setVisible(false);
    }
}