package fis.auth.domain.model;

public record TokenRequest(
        Integer userId,
        Integer rolId
) {
}
