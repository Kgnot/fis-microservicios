package uni.fis.multimedia.exception;


public class MultimediaExceptions {
    
    // Excepciones de validación
    public static class EmptyFileException extends RuntimeException {
        public EmptyFileException(String message) { super(message); }
    }

    public static class InvalidFileNameException extends RuntimeException {
        public InvalidFileNameException(String message) { super(message); }
    }

    public static class InvalidFileTypeException extends RuntimeException {
        public InvalidFileTypeException(String message) { super(message); }
    }

    public static class InvalidIdException extends RuntimeException {
        public InvalidIdException(String message) { super(message); }
    }

    // Excepciones de seguridad
    public static class InfectedFileException extends SecurityException {
        public InfectedFileException(String message) { super(message); }
    }

    // Excepciones de infraestructura
    public static class FileStorageException extends RuntimeException {
        public FileStorageException(String message, Throwable cause) { super(message, cause); }
    }

    public static class ClamAVConnectionException extends RuntimeException {
        public ClamAVConnectionException(String message, Throwable cause) { super(message, cause); }
    }

    public static class VirusScanException extends RuntimeException {
        public VirusScanException(String message, Throwable cause) { super(message, cause); }
    }

    public static class DataAccessException extends RuntimeException {
        public DataAccessException(String message, Throwable cause) { super(message, cause); }
    }

    // Excepciones de dominio
    public static class MultimediaNotFoundException extends RuntimeException {
        public MultimediaNotFoundException(String message) { super(message); }
    }
}