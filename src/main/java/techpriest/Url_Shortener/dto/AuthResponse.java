package techpriest.Url_Shortener.dto;

/**
 * Returned after registration/login: the user's public data plus their tokens.
 */
public record AuthResponse(UserResponseDto user, TokenPair tokens) {
}
