package cl.duoc.backendmpaz.service;

import cl.duoc.backendmpaz.Repository.RecordatorioRepository;
import cl.duoc.backendmpaz.model.Recordatorio;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecordatorioService {

    private final RecordatorioRepository repository;

    public RecordatorioService(RecordatorioRepository repository) {
        this.repository = repository;
    }

    public List<Recordatorio> listarTodos() {
        return repository.findAll();
    }

    public List<Recordatorio> listarPorUsuario(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }

    public Recordatorio crear(Recordatorio recordatorio) {
        recordatorio.setId(null);
        return repository.save(recordatorio);
    }

    public Optional<Recordatorio> actualizar(Long id, Recordatorio datos) {
        return repository.findById(id).map(existente -> {
            existente.setTitulo(datos.getTitulo());
            existente.setDescripcion(datos.getDescripcion());
            existente.setFecha(datos.getFecha());
            existente.setCompletado(datos.isCompletado());
            return repository.save(existente);
        });
    }

    public boolean eliminar(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}