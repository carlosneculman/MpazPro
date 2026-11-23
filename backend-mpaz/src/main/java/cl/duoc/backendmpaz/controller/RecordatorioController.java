package cl.duoc.backendmpaz.controller;

import cl.duoc.backendmpaz.model.Recordatorio;
import cl.duoc.backendmpaz.service.RecordatorioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recordatorios")
@CrossOrigin(origins = "*")
public class RecordatorioController {

    private final RecordatorioService service;

    public RecordatorioController(RecordatorioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Recordatorio>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Recordatorio>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(service.listarPorUsuario(usuarioId));
    }

    @PostMapping
    public ResponseEntity<Recordatorio> crear(@RequestBody Recordatorio recordatorio) {
        Recordatorio creado = service.crear(recordatorio);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Recordatorio datos) {
        return service.actualizar(id, datos)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Recordatorio no encontrado")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (!service.eliminar(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Recordatorio no encontrado"));
        }
        return ResponseEntity.ok(Map.of("mensaje", "Recordatorio eliminado"));
    }
}
