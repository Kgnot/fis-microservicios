package fis.auth.infrastructure.dto.request;

public record SignInRequest(
        String username,
        String password,
        String email,
        String rol
) {
}
