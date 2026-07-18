package techpriest.Url_Shortener.dto;

/**
 * Generic response for endpoints whose only payload is a status message.
 */
public record MessageResponse(String message) {
}
