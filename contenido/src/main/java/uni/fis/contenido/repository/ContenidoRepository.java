package uni.fis.contenido.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.fis.contenido.entity.ContenidoEntity;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface ContenidoRepository extends JpaRepository<ContenidoEntity, Integer> {
    List<ContenidoEntity> findByFecha(LocalDateTime fecha);
    List<ContenidoEntity> findByUsuario(Integer usuario);
}