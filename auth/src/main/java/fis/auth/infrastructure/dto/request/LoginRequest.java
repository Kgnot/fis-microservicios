package fis.auth.infrastructure.dto.request;

public record LoginRequest(
        String name,
        String password
) {
}
