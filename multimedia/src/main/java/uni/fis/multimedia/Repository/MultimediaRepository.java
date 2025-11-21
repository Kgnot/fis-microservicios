package uni.fis.multimedia.repository;


import org.springframework.data.jpa.Repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import uni.fis.multimedia.entity.MultimediaEntity;
import uni.fis.contenido.entity.PublicacionEntity;
import java.util.List;

@Repository
public interface MultimediaRepository extends JpaRepository<MultimediaEntity, Long> {
    List<MultimediaEntity> findByPublicacion(PublicacionEntity publicacion);
}