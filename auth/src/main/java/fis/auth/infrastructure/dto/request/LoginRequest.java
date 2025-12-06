package fis.auth.infrastructure.dto.request;

public record LoginRequest(
        String email,
        String password
) {
}
