package cl.duoc.backendmpaz.controller;

import cl.duoc.backendmpaz.Repository.RolRepository;
import cl.duoc.backendmpaz.Repository.UsuarioRepository;
import cl.duoc.backendmpaz.model.Rol;
import cl.duoc.backendmpaz.model.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    public AuthController(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (request.email() == null || request.contrasena() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Faltan credenciales"));
        }

        String correo = request.email().trim().toLowerCase();

        return usuarioRepository.findByCorreo(correo)
                .filter(u -> u.getClave().equals(request.contrasena()))
                .<ResponseEntity<?>>map(u -> {
                    String token = UUID.randomUUID().toString();
                    UsuarioLogin usuarioLogin = new UsuarioLogin(
                            String.valueOf(u.getId()),
                            u.getNombre(),
                            u.getRol().getId()
                    );
                    return ResponseEntity.ok(new LoginResponse("Inicio de sesión exitoso", token, usuarioLogin));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Correo o contraseña incorrectos")));
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody RegistroRequest request) {
        if (request.email() == null || request.email().isBlank()
                || request.nombre_user() == null || request.nombre_user().isBlank()
                || request.contrasena() == null || request.contrasena().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Los datos ingresados no son válidos"));
        }

        String correo = request.email().trim().toLowerCase();

        if (usuarioRepository.existsByCorreo(correo)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "El correo ya se encuentra registrado"));
        }

        Integer rolId = (request.id_rol() != null) ? request.id_rol() : 3;
        Rol rol = rolRepository.findById(rolId).orElse(null);
        if (rol == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El rol indicado no existe"));
        }

        Usuario nuevo = new Usuario(
                request.nombre_user().trim(),
                correo,
                request.contrasena(),
                request.rut_user(),
                request.telefono_user(),
                rol
        );
        Usuario guardado = usuarioRepository.save(nuevo);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "mensaje", "Usuario registrado correctamente",
                        "id_user", String.valueOf(guardado.getId())
                ));
    }

    @GetMapping("/usuarios")
    public List<UsuarioLogin> listarUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(u -> new UsuarioLogin(
                        String.valueOf(u.getId()),
                        u.getNombre(),
                        u.getRol().getId()
                ))
                .toList();
    }

    public record LoginRequest(String email, String contrasena) {}
    public record UsuarioLogin(String id_user, String nombre, Integer rol) {}
    public record LoginResponse(String mensaje, String token, UsuarioLogin usuario) {}
    public record RegistroRequest(
            String email,
            String nombre_user,
            String contrasena,
            String rut_user,
            String telefono_user,
            Integer id_rol
    ) {}
}