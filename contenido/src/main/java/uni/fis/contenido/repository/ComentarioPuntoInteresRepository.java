package uni.fis.contenido.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.fis.contenido.entity.ComentarioPuntoInteresEntity;
import java.util.List;



@Repository
public interface ComentarioPuntoInteresRepository extends JpaRepository<ComentarioPuntoInteresEntity, Long> {
    List<ComentarioPuntoInteresEntity> findByIdPunto(Long idPunto);

}
