package uni.fis.contenido.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.fis.contenido.entity.ComentarioEntity;
import uni.fis.contenido.entity.PublicacionEntity;
import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<ComentarioEntity, Integer> {
    List<ComentarioEntity> findByPublicacion(PublicacionEntity publicacion);
}