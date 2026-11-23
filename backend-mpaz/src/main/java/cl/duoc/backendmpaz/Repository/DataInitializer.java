package cl.duoc.backendmpaz.Repository;

import cl.duoc.backendmpaz.model.Rol;
import cl.duoc.backendmpaz.model.Usuario;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;

    public DataInitializer(RolRepository rolRepository, UsuarioRepository usuarioRepository) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) {
        Rol utp = rolRepository.findById(1)
                .orElseGet(() -> rolRepository.save(new Rol(1, "UTP")));
        Rol docente = rolRepository.findById(2)
                .orElseGet(() -> rolRepository.save(new Rol(2, "DOCENTE")));
        Rol estudiante = rolRepository.findById(3)
                .orElseGet(() -> rolRepository.save(new Rol(3, "ESTUDIANTE")));

        if (!usuarioRepository.existsByCorreo("utp@mpaz.cl")) {
            usuarioRepository.save(new Usuario("Coordinador UTP", "utp@mpaz.cl", "123456", utp));
        }
        if (!usuarioRepository.existsByCorreo("profesor@mpaz.cl")) {
            usuarioRepository.save(new Usuario("Docente 3° Básico", "profesor@mpaz.cl", "123456", docente));
        }
        if (!usuarioRepository.existsByCorreo("estudiante@mpaz.cl")) {
            usuarioRepository.save(new Usuario("Estudiante Prueba", "estudiante@mpaz.cl", "123456", estudiante));
        }
    }
}
