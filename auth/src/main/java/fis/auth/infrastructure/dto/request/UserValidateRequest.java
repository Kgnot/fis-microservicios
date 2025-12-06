package fis.auth.infrastructure.dto.request;

public record UserValidateRequest(
        String email,
        String password
) {
}
