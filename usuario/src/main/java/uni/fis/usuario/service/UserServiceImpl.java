package uni.fis.usuario.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import uni.fis.usuario.dto.UserDto;
import uni.fis.usuario.dto.request.UserRequest;
import uni.fis.usuario.entity.DocumentoEntity;
import uni.fis.usuario.entity.PasswordEntity;
import uni.fis.usuario.entity.UsuarioEntity;
import uni.fis.usuario.error.InvalidUserDataException;
import uni.fis.usuario.error.UserAlreadyExistsException;
import uni.fis.usuario.error.UserNotFoundException;
import uni.fis.usuario.mapper.DocumentoMapper;
import uni.fis.usuario.mapper.UserMapper;
import uni.fis.usuario.repository.DocumentoRepository;
import uni.fis.usuario.repository.PasswordRepository;
import uni.fis.usuario.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordRepository passwordRepository;
    private final EntityManager entityManager;
    private final DocumentoRepository documentoRepository;

    public UserServiceImpl(UserRepository userRepository,
            PasswordRepository passwordRepository,
            EntityManager entityManager,
            DocumentoRepository documentoRepository) {
        this.userRepository = userRepository;
        this.passwordRepository = passwordRepository;
        this.entityManager = entityManager;
        this.documentoRepository = documentoRepository;
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
        UsuarioEntity entity = userRepository.findById(id)
                .orElseThrow(() -> UserNotFoundException.byId(id));
        return Optional.of(UserMapper.entityToDto(entity));
    }

    @Override
    public UserDto save(UserRequest user) {
        // Validar campos requeridos primero
        validateRequiredFields(user);

        // Verificar email único
        if (userRepository.existsByEmail(user.email())) {
            log.info("Email ya existe: {}", user.email());
            throw UserAlreadyExistsException.byEmail(user.email());
        }

        try {
            // creamos primero el documento
            DocumentoEntity documento = DocumentoMapper.toEntityForCreate(user.documento());
            documento = documentoRepository.save(documento); // ← AQUÍ se genera el ID correcto

            UsuarioEntity entity = UserMapper.requestToEntity(user, documento);
            var userCreated = userRepository.save(entity);

            PasswordEntity entityPassword = new PasswordEntity(
                    null,
                    LocalDateTime.now(),
                    user.password(), // Aquí deberías encriptar la contraseña
                    userCreated.getId());
            passwordRepository.save(entityPassword);
            return UserMapper.entityToDto(entity);

        } catch (DataIntegrityViolationException e) {
            // Manejar violación de integridad (documento único, etc.)
            if (e.getMessage().contains("documento")) {
                throw UserAlreadyExistsException.byDocument(user.documento().numeroDocumento());
            }

            // Resetear secuencia si hay conflicto de ID
            resetSequence();
            DocumentoEntity documento = DocumentoMapper.toEntityForCreate(user.documento());
            documento = documentoRepository.save(documento); // ← AQUÍ se genera el ID correcto

            // Reintentar
            UsuarioEntity entity = UserMapper.requestToEntity(user, documento);
            var userCreated = userRepository.save(entity);

            PasswordEntity entityPassword = new PasswordEntity(
                    null,
                    LocalDateTime.now(),
                    user.password(),
                    userCreated.getId());
            passwordRepository.save(entityPassword);
            return UserMapper.entityToDto(entity);
        }
    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserMapper::entityToDto);
    }

    private void validateRequiredFields(UserRequest request) {
        log.info("Validando campos requeridos {}", request);
        if (request.name() == null || request.name().trim().isEmpty()) {
            throw InvalidUserDataException.missingField("name");
        }
        if (request.apellido1() == null || request.apellido1().trim().isEmpty()) {
            throw InvalidUserDataException.missingField("apellido1");
        }
        if (request.fechaNacimiento() == null || request.fechaNacimiento().trim().isEmpty()) {
            throw InvalidUserDataException.missingField("fechaNacimiento");
        }
        if (request.documento() == null || request.documento().numeroDocumento().trim().isEmpty()) {
            throw InvalidUserDataException.missingField("documento");
        }
        if (request.email() == null || request.email().trim().isEmpty()) {
            throw InvalidUserDataException.missingField("email");
        }
        if (request.password() == null || request.password().trim().isEmpty()) {
            throw InvalidUserDataException.missingField("password");
        }

        // Validar formato de email
        if (!isValidEmail(request.email())) {
            throw InvalidUserDataException.invalidField("email", request.email());
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private void resetSequence() {
        try {
            Query query = entityManager.createNativeQuery(
                    "SELECT setval('usuario_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM usuario))");
            query.getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException("Error al resetear secuencia: " + e.getMessage());
        }
    }
}