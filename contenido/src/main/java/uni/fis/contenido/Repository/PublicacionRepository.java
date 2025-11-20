package uni.fis.contenido.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import uni.fis.contenido.Entity.PublicacionEntity;
import uni.fis.foro.Entity.ForoEntity;
import java.util.List;


@Repository
public interface PublicacionRepository extends JpaRepository<PublicacionEntity, Long> {
    List<PublicacionEntity> findByForo(ForoEntity foro);
}