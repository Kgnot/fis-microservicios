package uni.fis.foro.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import uni.fis.foro.entity.ForoEntity;


@Repository
public interface ForoRepository extends JpaRepository<ForoEntity, Long> {}