package fis.auth.domain.error;

public class MissingRequiredFieldException extends SignInException {
    public MissingRequiredFieldException(String message) {
        super(message);
    }
}
