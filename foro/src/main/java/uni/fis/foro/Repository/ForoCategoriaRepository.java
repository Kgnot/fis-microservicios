package uni.fis.foro.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.fis.foro.entity.CategoriaEntity;
import uni.fis.foro.entity.ForoCategoriaEntity;
import java.util.List;

@Repository
public interface ForoCategoriaRepository extends JpaRepository<ForoCategoriaEntity, Long> {
    List<ForoCategoriaEntity> findByCategoria(CategoriaEntity categoria);
}