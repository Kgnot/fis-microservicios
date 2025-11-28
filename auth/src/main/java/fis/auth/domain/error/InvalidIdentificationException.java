package fis.auth.domain.error;

public class InvalidIdentificationException extends SignInException {
  public InvalidIdentificationException(String message) {
    super(message);
  }
}
