package fis.auth.infrastructure.error.global;

import fis.auth.domain.error.InvalidIdentificationException;
import fis.auth.domain.error.MissingRequiredFieldException;
import fis.auth.domain.error.TutorApprovalPendingException;
import fis.auth.infrastructure.dto.response.api.ApiResponse;
import fis.auth.infrastructure.error.NoUserFoundError;
import fis.auth.infrastructure.error.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(NoUserFoundError.class)
    public ResponseEntity<ApiResponse<?>> handleNoUserFoundError(NoUserFoundError err) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(err.getMessage(), 404));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Error interno del servidor", 500));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage(), 400));
    }

    // SIGN IN:
    // ========== EXCEPCIONES DE INFRAESTRUCTURA ==========
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<?>> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ex.getMessage(), 409));
    }

    // ========== EXCEPCIONES DE DOMINIO ==========
    @ExceptionHandler(MissingRequiredFieldException.class)
    public ResponseEntity<ApiResponse<?>> handleMissingFields(MissingRequiredFieldException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage(), 400));
    }

    @ExceptionHandler(TutorApprovalPendingException.class)
    public ResponseEntity<ApiResponse<?>> handleTutorApproval(TutorApprovalPendingException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(ex.getMessage(), 403));
    }

    @ExceptionHandler(InvalidIdentificationException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidIdentification(InvalidIdentificationException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY) // 422
                .body(ApiResponse.error(ex.getMessage(), 422));
    }

}
