package uni.fis.usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uni.fis.usuario.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    boolean existsByEmail(String email);
}
