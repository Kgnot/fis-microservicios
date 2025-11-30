package fis.auth.domain.model;

public record TokenRequest(
                Integer userId,
                String rol, // este va a ser el Rol
                String email) {
}
