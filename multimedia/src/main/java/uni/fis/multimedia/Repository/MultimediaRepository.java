package uni.fis.multimedia.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.fis.multimedia.entity.MultimediaEntity;
import java.util.List;

@Repository
public interface MultimediaRepository extends JpaRepository<MultimediaEntity, Long> {
    List<MultimediaEntity> findByPublicacion(Long publicacion);
}