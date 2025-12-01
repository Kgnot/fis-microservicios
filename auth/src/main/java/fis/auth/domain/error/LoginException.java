package fis.auth.domain.error;

public abstract class LoginException extends RuntimeException {
    public LoginException(String message) {
        super(message);
    }
}
