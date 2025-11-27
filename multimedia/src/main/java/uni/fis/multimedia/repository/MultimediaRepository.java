package uni.fis.multimedia.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.fis.multimedia.entity.MultimediaEntity;

@Repository
public interface MultimediaRepository extends JpaRepository<MultimediaEntity, Long> {

    MultimediaEntity findMultimediaEntityById(Long id);

}
