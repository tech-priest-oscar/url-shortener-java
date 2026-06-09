package techpriest.Url_Shortener.services;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import techpriest.Url_Shortener.dto.TokenPair;
import techpriest.Url_Shortener.models.User;

@Service
public class JwtService {

    private static final long ACCESS_TOKEN_TTL_SECONDS = 24 * 60 * 60;       
    private static final long REFRESH_TOKEN_TTL_SECONDS = 7 * 24 * 60 * 60; 

    private final SecretKey signingKey;

    public JwtService(@Value("${jwt.secret}") String secret) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Creates an access + refresh token pair for the user. Both tokens share the
     * same {@code jti} (so the pair can be linked/revoked together) and carry the
     * user's id as the subject. They differ only in how long they live.
     */
    public TokenPair generateTokens(User user) {
        String jti = UUID.randomUUID().toString();
        Instant now = Instant.now();

        String accessToken = buildToken(user, jti, now, ACCESS_TOKEN_TTL_SECONDS);
        String refreshToken = buildToken(user, jti, now, REFRESH_TOKEN_TTL_SECONDS);

        return new TokenPair(accessToken, refreshToken);
    }

    private String buildToken(User user, String jti, Instant issuedAt, long ttlSeconds) {
        Instant expiresAt = issuedAt.plusSeconds(ttlSeconds);
        return Jwts.builder()
                .id(jti)                            
                .subject(user.getId().toString())    
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey)
                .compact();
    }
}
