package uni.fis.foro.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import uni.fis.foro.Entity.ForoEntity;


@Repository
public interface ForoRepository extends JpaRepository<ForoEntity, Long> {}