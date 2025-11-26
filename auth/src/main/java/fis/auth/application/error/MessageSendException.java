package fis.auth.application.error;

public class MessageSendException extends RuntimeException {
    public MessageSendException(String message) {
        super(message);
    }
}
