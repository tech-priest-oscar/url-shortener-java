package techpriest.Url_Shortener.services;

import techpriest.Url_Shortener.dto.TokenPair;
import techpriest.Url_Shortener.models.User;

public interface JwtService {

    /**
     * Creates an access + refresh token pair for the user. Both tokens share the
     * same {@code jti} (so the pair can be linked/revoked together) and carry the
     * user's id as the subject. They differ in their {@code type} claim and lifespan.
     */
    TokenPair generateTokens(User user);

    /** Validates an access token and returns the user id. Used to guard requests. */
    String validateAccessTokenAndGetUserId(String token);

    /** Validates a refresh token and returns the user id. Used by the refresh endpoint. */
    String validateRefreshTokenAndGetUserId(String token);
}
