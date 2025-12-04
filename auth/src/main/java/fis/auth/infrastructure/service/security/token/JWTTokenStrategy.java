package fis.auth.infrastructure.service.security.token;

import fis.auth.domain.service.TokenStrategy;
import fis.auth.domain.model.Token;
import fis.auth.domain.model.TokenRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
public class JWTTokenStrategy implements TokenStrategy {

    @Value("${SECRET.KEY}")
    private String SECRET_KEY;
    @Value("${JWT.TOKEN.EXPIRATION}")
    private Long jwtExpiration;
    @Value("${JWT.TOKEN.REFRESH.EXPIRATION}")
    private Long refreshTokenExpiration;

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public Token generate(TokenRequest tokenRequest) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", tokenRequest.rolId()); // TODO - hay que buscar aqui el token

        String accessToken = generateToken(claims, tokenRequest.userId(), jwtExpiration); // Usaremos el id que es único
        String refreshToken = generateToken(claims, tokenRequest.userId(), refreshTokenExpiration);
        Instant expiration = Instant.now().plusSeconds(jwtExpiration);
        return new Token(accessToken, refreshToken, expiration, true);
    }

    @Override
    public Token validate(String token) {
        try {
            Claims claims = getAllClaims(token);
            String username = claims.getSubject();
            Date expirationDate = claims.getExpiration();
            Instant expirationInstante = expirationDate.toInstant();

            if (StringUtils.isBlank(username) || expirationInstante.isBefore(Instant.now())) {
                throw new JwtException("Token inválido o expirado");
            }

            return new Token(null, null, null, true);

        } catch (JwtException e) {
            throw new JwtException("Error al validar token: " + e.getMessage(), e);
        }
    }

    // Métodos privados:
    private String generateToken(Map<String, Object> extraClaims, Integer subject, Long expiration) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(subject.toString()) // lo pondremos en string porque es necesairo pero al ser el único
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey())
                .compact();
    }

    private Claims getAllClaims(String token) {
        if (StringUtils.isBlank(token)) {
            throw new JwtException("Token vacío o nulo");
        }
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Claims internalClaims(String token) {
        return getAllClaims(token); // sigue siendo private internamente
    }

    private <T> Optional<T> getClaim(String token, Function<Claims, T> claimResolver) {
        try {
            final Claims claims = getAllClaims(token);
            return Optional.ofNullable(claimResolver.apply(claims));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private boolean isNotExpired(String token) {
        Date expiration = getClaim(token, Claims::getExpiration).orElse(null);
        return expiration != null && expiration.after(new Date());
    }
}
