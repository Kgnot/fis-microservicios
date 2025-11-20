package uni.fis.contenido.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import uni.fis.contenido.Entity.CategoriaEntity;
import uni.fis.contenido.Entity.ContenidoEntity;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface ContenidoRepository extends JpaRepository<ContenidoEntity, Long> {
    List<ContenidoEntity> findByFecha(LocalDateTime fecha);
    List<ContenidoEntity> findByUsuario(String usuario);
}