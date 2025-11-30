package uni.fis.contenido.exception;

public class UsuarioNoExisteException extends RuntimeException {
    public UsuarioNoExisteException(Integer id) {
        super("No existe el usuario con ID: " + id);
    }
}
