package fis.auth.infrastructure.error;

public class EmailApiErrorException extends RuntimeException {
    private EmailApiErrorException(String message) {
        super(message);
    }

    public static EmailApiErrorException of(String message) {
        return new EmailApiErrorException(message);
    }
}
