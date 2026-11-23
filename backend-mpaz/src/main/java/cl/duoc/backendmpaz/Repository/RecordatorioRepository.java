package cl.duoc.backendmpaz.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.duoc.backendmpaz.model.Recordatorio;

import java.util.List;

@Repository 
public interface RecordatorioRepository extends JpaRepository<Recordatorio, Long>{
    List<Recordatorio> findByUsuarioId(Long usuarioId);
    
}
