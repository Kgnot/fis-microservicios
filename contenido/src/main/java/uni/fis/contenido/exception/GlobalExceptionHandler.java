package uni.fis.contenido.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import uni.fis.contenido.dto.ErrorDTO;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PublicacionNoEncontradaException.class)
    public ResponseEntity<?> handlePublicacionNoEncontrada(PublicacionNoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorDTO(LocalDateTime.now(), 404, ex.getMessage())
        );
    }

    @ExceptionHandler(LikeDuplicadoException.class)
    public ResponseEntity<?> handleLikeDuplicado(LikeDuplicadoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorDTO(LocalDateTime.now(), 400, ex.getMessage())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorDTO(LocalDateTime.now(), 500, ex.getMessage())
        );
    }

    @ExceptionHandler(ComentarioNoEncontradoException.class)
    public ResponseEntity<?> handleComentarioNoEncontrado(ComentarioNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorDTO(LocalDateTime.now(), 404, ex.getMessage())
        );
    }

    @ExceptionHandler(ContenidoNoValidoException.class)
    public ResponseEntity<?> handleContenidoNoValido(ContenidoNoValidoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorDTO(LocalDateTime.now(), 400, ex.getMessage())
        );
    }

    @ExceptionHandler(PuntoInteresNoValidoException.class)
    public ResponseEntity<?> handlePuntoInteresNoValido(PuntoInteresNoValidoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorDTO(LocalDateTime.now(), 400, ex.getMessage())
        );
    }



}
