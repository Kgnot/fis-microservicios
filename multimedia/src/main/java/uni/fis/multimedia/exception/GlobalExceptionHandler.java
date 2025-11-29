package uni.fis.multimedia.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import jakarta.servlet.http.HttpServletRequest;
import uni.fis.multimedia.dto.ErrorResponseDTO;
import uni.fis.multimedia.exception.MultimediaExceptions.ClamAVConnectionException;
import uni.fis.multimedia.exception.MultimediaExceptions.DataAccessException;
import uni.fis.multimedia.exception.MultimediaExceptions.EmptyFileException;
import uni.fis.multimedia.exception.MultimediaExceptions.FileStorageException;
import uni.fis.multimedia.exception.MultimediaExceptions.InfectedFileException;
import uni.fis.multimedia.exception.MultimediaExceptions.InvalidFileNameException;
import uni.fis.multimedia.exception.MultimediaExceptions.InvalidFileTypeException;
import uni.fis.multimedia.exception.MultimediaExceptions.InvalidIdException;
import uni.fis.multimedia.exception.MultimediaExceptions.MultimediaNotFoundException;
import uni.fis.multimedia.exception.MultimediaExceptions.VirusScanException;




@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({EmptyFileException.class, InvalidFileNameException.class, 
                      InvalidFileTypeException.class, InvalidIdException.class})
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(
            RuntimeException ex, WebRequest request) {
        
        logger.warn("Validación fallida: {}", ex.getMessage());
        
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .codigo("VALIDACION_FALLIDA")
                .mensaje(ex.getMessage())
                .path(getRequestPath(request))
                .build();
                
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(InfectedFileException.class)
    public ResponseEntity<ErrorResponseDTO> handleInfectedFileException(
            InfectedFileException ex, WebRequest request) {
        
        logger.warn("Archivo infectado detectado: {}", ex.getMessage());
        
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .codigo("ARCHIVO_INFECTADO")
                .mensaje(ex.getMessage())
                .path(getRequestPath(request))
                .build();
                
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    @ExceptionHandler(MultimediaNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFoundException(
            MultimediaNotFoundException ex, WebRequest request) {
        
        logger.warn("Recurso no encontrado: {}", ex.getMessage());
        
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .codigo("NO_ENCONTRADO")
                .mensaje(ex.getMessage())
                .path(getRequestPath(request))
                .build();
                
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler({FileStorageException.class, ClamAVConnectionException.class, 
                      VirusScanException.class, DataAccessException.class})
    public ResponseEntity<ErrorResponseDTO> handleServiceExceptions(
            RuntimeException ex, WebRequest request) {
        
        logger.error("Error interno del servicio: {}", ex.getMessage(), ex);
        
        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .codigo("ERROR_SERVICIO")
                .mensaje("Error interno del servidor")
                .path(getRequestPath(request))
                .build();
                
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    // Método auxiliar para obtener el path de la request
    private String getRequestPath(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();
            return servletRequest.getRequestURI();
        }
        return "unknown";
    }
}