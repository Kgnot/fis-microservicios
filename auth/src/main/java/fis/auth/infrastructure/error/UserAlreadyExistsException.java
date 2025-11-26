package fis.auth.infrastructure.error;

public class UserAlreadyExistsException extends RuntimeException {
  private UserAlreadyExistsException(String message) {
    super(message);
  }
  public static UserAlreadyExistsException byEmail(String email) {
    return new UserAlreadyExistsException(
            String.format("Ya existe un usuario con el email: %s", email)
    );
  }

  public static UserAlreadyExistsException byIdentification(String identification) {
    return new UserAlreadyExistsException(
            String.format("Ya existe un usuario con la identificación: %s", identification)
    );
  }
}