package uni.fis.contenido.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import uni.fis.contenido.entity.ComentarioPuntoInteresEntity;



@Repository
public interface ComentarioPuntoInteresRepository extends JpaRepository<ComentarioPuntoInteresEntity, Long> {
    List<ComentarioPuntoInteresEntity> findByIdPunto(Long idPunto);

}
