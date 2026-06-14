package techpriest.Url_Shortener.services;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import techpriest.Url_Shortener.dto.TokenPair;
import techpriest.Url_Shortener.models.User;

@Service
public class JwtService {

    private static final long ACCESS_TOKEN_TTL_SECONDS = 24 * 60 * 60;       // 24 hours
    private static final long REFRESH_TOKEN_TTL_SECONDS = 7 * 24 * 60 * 60;  // 7 days

    private static final String CLAIM_TYPE = "type";
    private static final String TYPE_ACCESS = "access";
    private static final String TYPE_REFRESH = "refresh";

    private final SecretKey signingKey;

    public JwtService(@Value("${jwt.secret}") String secret) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Creates an access + refresh token pair for the user. Both tokens share the
     * same {@code jti} (so the pair can be linked/revoked together) and carry the
     * user's id as the subject. They differ in their {@code type} claim and lifespan.
     */
    public TokenPair generateTokens(User user) {
        String jti = UUID.randomUUID().toString();
        Instant now = Instant.now();

        String accessToken = buildToken(user, jti, now, ACCESS_TOKEN_TTL_SECONDS, TYPE_ACCESS);
        String refreshToken = buildToken(user, jti, now, REFRESH_TOKEN_TTL_SECONDS, TYPE_REFRESH);

        return new TokenPair(accessToken, refreshToken);
    }

    /** Validates an access token and returns the user id. Used to guard requests. */
    public String validateAccessTokenAndGetUserId(String token) {
        return validateTokenOfType(token, TYPE_ACCESS);
    }

    /** Validates a refresh token and returns the user id. Used by the refresh endpoint. */
    public String validateRefreshTokenAndGetUserId(String token) {
        return validateTokenOfType(token, TYPE_REFRESH);
    }

    private String validateTokenOfType(String token, String expectedType) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String actualType = claims.get(CLAIM_TYPE, String.class);
        if (!expectedType.equals(actualType)) {
            throw new JwtException("Expected a " + expectedType + " token but got: " + actualType);
        }
        return claims.getSubject();
    }

    private String buildToken(User user, String jti, Instant issuedAt, long ttlSeconds, String type) {
        Instant expiresAt = issuedAt.plusSeconds(ttlSeconds);
        return Jwts.builder()
                .id(jti)                               // jti — same on both tokens
                .subject(user.getId().toString())      // the user id
                .claim(CLAIM_TYPE, type)               // "access" or "refresh"
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey)
                .compact();
    }
}
