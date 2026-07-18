package techpriest.Url_Shortener.dto;

/**
 * Returned after registration: the user's public data plus a next-step message.
 * Tokens are only issued after the email is verified (see AuthResponse).
 */
public record RegistrationResponse(UserResponseDto user, String message) {
}
