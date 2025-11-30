package fis.auth.domain.model;

import java.util.List;

public record TokenRequest(
        Integer userId,
        List<String> roles , // este va a ser el Rol
        String email
) {
}
