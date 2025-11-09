package fis.auth.domain.service;

public interface EncryptStrategy {

    String encrypt(String rawPassword);

    boolean matches(String rawPassword, String encryptedPassword);
}
