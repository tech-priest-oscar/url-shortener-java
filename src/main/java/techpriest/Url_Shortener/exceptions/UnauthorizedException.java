package techpriest.Url_Shortener.exceptions;

/**
 * Thrown when a request is missing a valid access token.
 * The GlobalExceptionHandler turns this into an HTTP 401 response.
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
