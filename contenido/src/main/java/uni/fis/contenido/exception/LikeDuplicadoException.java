package uni.fis.contenido.exception;

public class LikeDuplicadoException extends RuntimeException {
    public LikeDuplicadoException(Integer usuario, Integer publicacion) {
        super("El usuario " + usuario + " ya dio like a la publicación " + publicacion);
    }
}
