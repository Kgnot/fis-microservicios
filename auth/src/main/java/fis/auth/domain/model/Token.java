package fis.auth.domain.model;

import java.time.Instant;

public record Token(
        String accessToken,
        String refreshToken,
        Instant expiresAt,
        boolean isValid
) {


}
