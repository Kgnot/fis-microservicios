package uni.fis.multimedia.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import uni.fis.multimedia.Entity.MultimediaEntity;
import uni.fis.contenido.Entity.PublicacionEntity;
import java.util.List;

@Repository
public interface MultimediaRepository extends JpaRepository<MultimediaEntity, Long> {
    List<MultimediaEntity> findByPublicacion(PublicacionEntity publicacion);
}