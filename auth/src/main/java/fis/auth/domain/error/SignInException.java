package fis.auth.domain.error;

public abstract class SignInException extends RuntimeException {
    public SignInException(String message) {
        super(message);
    }
}
