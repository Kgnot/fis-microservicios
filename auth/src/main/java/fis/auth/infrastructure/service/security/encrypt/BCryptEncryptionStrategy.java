package fis.auth.infrastructure.service.security.encrypt;

import fis.auth.domain.service.EncryptStrategy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptEncryptionStrategy implements EncryptStrategy {

    private final BCryptPasswordEncoder encoder;

    public BCryptEncryptionStrategy() {
        this.encoder = new BCryptPasswordEncoder();
    }

    @Override
    public String encrypt(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encryptedPassword) {
        return encoder.matches(rawPassword, encryptedPassword);
    }
}