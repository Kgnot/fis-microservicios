package uni.fis.usuario.dto.request;

public record TokenRequest(
        Integer userId,
        Integer rolId
) {
}
