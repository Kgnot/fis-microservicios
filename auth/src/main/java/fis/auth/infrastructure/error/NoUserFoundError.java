package fis.auth.infrastructure.error;

public final class NoUserFoundError extends RuntimeException {
    private NoUserFoundError(String message) {
        super(message);
    }

    public static NoUserFoundError of(String message) {
        return new NoUserFoundError(message);
    }

}
