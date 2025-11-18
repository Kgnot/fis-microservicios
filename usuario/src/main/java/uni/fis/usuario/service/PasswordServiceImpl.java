package uni.fis.usuario.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import uni.fis.usuario.entity.PasswordEntity;
import uni.fis.usuario.repository.PasswordRepository;
import uni.fis.usuario.repository.UserRepository;

import java.util.Optional;

@Service
public class PasswordServiceImpl implements PasswordService {

    private final PasswordRepository repository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public PasswordServiceImpl(PasswordRepository repository,
                               UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public boolean validate(String email, String password) {
        // Buscamos el usuario
        var usuario = userRepository.findByEmail(email);
        if (usuario.isEmpty()) {
            return false;
        }

        // Buscamos la última contraseña
        Optional<PasswordEntity> ultima = repository.findMasRecienteById(usuario.get().getId());

        return ultima
                .map(passwordEntity -> passwordEncoder.matches(password, passwordEntity.getChars()))
                .orElse(false);
    }
}