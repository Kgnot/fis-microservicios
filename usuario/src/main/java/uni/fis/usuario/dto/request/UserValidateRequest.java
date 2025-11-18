package uni.fis.usuario.dto.request;

public record UserValidateRequest(
        Integer id,
        String password
) {
}
