package uni.fis.contenido.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import uni.fis.contenido.entity.PublicacionEntity;
import uni.fis.foro.entity.ForoEntity;
import java.util.List;


@Repository
public interface PublicacionRepository extends JpaRepository<PublicacionEntity, Long> {
    List<PublicacionEntity> findByForo(ForoEntity foro);
}