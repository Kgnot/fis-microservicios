package fis.auth.domain.model;

public record TokenRequest(
        String name,
        String rol
) {
}
