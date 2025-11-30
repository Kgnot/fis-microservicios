package uni.fis.contenido.exception;

public class PublicacionNoEncontradaException extends RuntimeException {
    public PublicacionNoEncontradaException(Integer id) {
        super("No existe la publicación con ID: " + id);
    }
}
