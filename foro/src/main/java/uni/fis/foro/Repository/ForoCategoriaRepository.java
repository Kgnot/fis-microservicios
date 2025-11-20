package uni.fis.foro.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import uni.fis.foro.Entity.CategoriaEntity;
import uni.fis.foro.Entity.ForoCategoriaEntity;
import java.util.List;

public interface ForoCategoriaRepository extends JpaRepository<ForoCategoriaEntity, Long> {
    List<ForoCategoriaEntity> findByCategoria(CategoriaEntity categoria);
}