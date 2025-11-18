package fis.auth.infrastructure.service.security.auth;

import fis.auth.infrastructure.service.security.token.JWTTokenStrategy;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenInfoExtractor {

    private final JWTTokenStrategy jwtTokenStrategy;

    public JwtTokenInfoExtractor(JWTTokenStrategy jwtTokenStrategy) {
        this.jwtTokenStrategy = jwtTokenStrategy;
    }

    public String getUsername(String token) {
        Claims claims = jwtTokenStrategy.internalClaims(token);
        return claims.getSubject();
    }

    public String getRole(String token) {
        Claims claims = jwtTokenStrategy.internalClaims(token);
        return claims.get("role", String.class);
    }
}

