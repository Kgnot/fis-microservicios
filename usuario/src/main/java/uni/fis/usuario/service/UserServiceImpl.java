package uni.fis.usuario.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import uni.fis.usuario.dto.UserDto;
import uni.fis.usuario.dto.request.UserRequest;
import uni.fis.usuario.entity.PasswordEntity;
import uni.fis.usuario.entity.UserEntity;
import uni.fis.usuario.mapper.UserMapper;
import uni.fis.usuario.repository.PasswordRepository;
import uni.fis.usuario.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordRepository passwordRepository;
    private final EntityManager entityManager;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordRepository passwordRepository,
                           EntityManager entityManager) {
        this.userRepository = userRepository;
        this.passwordRepository = passwordRepository;
        this.entityManager = entityManager;
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::entityToDto)
                .toList();
    }

    @Override
    public Optional<UserDto> findById(int id) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No está el usuario"));
        return Optional.of(UserMapper.entityToDto(entity));
    }

    @Override
    public UserDto save(UserRequest user) {
        if (userRepository.existsByEmail(user.email())) {
            throw new RuntimeException("El email ya está registrado");
        }

        try {
            UserEntity entity = UserMapper.requestToEntity(user);
            var userCreated = userRepository.save(entity);

            PasswordEntity entityPassword = new PasswordEntity(
                    null,
                    LocalDateTime.now(),
                    user.password(),
                    userCreated.getId()
            );
            passwordRepository.save(entityPassword);
            return UserMapper.entityToDto(entity);

        } catch (DataIntegrityViolationException e) {
            // Resetear secuencia si hay conflicto
            resetSequence();

            // Reintentar
            UserEntity entity = UserMapper.requestToEntity(user);
            var userCreated = userRepository.save(entity);

            PasswordEntity entityPassword = new PasswordEntity(
                    null,
                    LocalDateTime.now(),
                    user.password(),
                    userCreated.getId()
            );
            passwordRepository.save(entityPassword);
            return UserMapper.entityToDto(entity);
        }
    }

    private void resetSequence() {
        Query query = entityManager.createNativeQuery(
                "SELECT setval('usuario_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM usuario))"
        );
        query.getSingleResult();
    }

}
