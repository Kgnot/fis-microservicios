package uni.fis.contenido.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uni.fis.contenido.entity.PublicacionLikeEntity;

public interface PublicacionLikeRepository extends JpaRepository<PublicacionLikeEntity, Integer> {
    boolean existsByPublicacionIdAndIdUsuario(Integer idPublicacion, Integer idUsuario);
    void deleteByPublicacionIdAndIdUsuario(Integer idPublicacion, Integer idUsuario);
    int countByPublicacionId(Integer idPublicacion);
}
