package uni.fis.usuario.service;

import org.springframework.stereotype.Service;
import uni.fis.usuario.entity.PasswordEntity;
import uni.fis.usuario.repository.PasswordRepository;
import uni.fis.usuario.repository.UserRepository;

import java.util.Optional;

@Service
public class PasswordServiceImpl implements PasswordService {

    private final PasswordRepository repository;
    private final UserRepository userRepository;

    public PasswordServiceImpl(PasswordRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean validate(String email, String password) {

        // Buscamos el usuario primero
        var usuario = userRepository.findByEmail(email);
        if (usuario.isEmpty()) {
            return false;
        }

        // Buscamos la última contraseña
        Optional<PasswordEntity> ultima = repository.findMasRecienteById(usuario.get().getId());

        boolean isValid = ultima
                .map(passwordEntity -> passwordEntity.getChars().equals(password))
                .orElse(false);

        System.out.println("Resultado final de validación: " + isValid);
        return isValid;
    }
}
