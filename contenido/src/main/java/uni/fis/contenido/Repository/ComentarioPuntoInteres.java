package uni.fis.contenido.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import uni.fis.contenido.Entity.ComentarioPuntoInteresEntity;



@Repository
public interface ComentarioPuntoInteres extends JpaRepository<ComentarioPuntoInteresEntity, Long> {
    List<ComentarioPuntoInteresEntity> findByIdPunto(Long idPunto);

}
