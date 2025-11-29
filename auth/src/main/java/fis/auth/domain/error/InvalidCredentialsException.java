package fis.auth.domain.error;

public class InvalidCredentialsException extends LoginException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
