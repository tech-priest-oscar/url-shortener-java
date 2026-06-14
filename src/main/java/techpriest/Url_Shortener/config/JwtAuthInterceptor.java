package techpriest.Url_Shortener.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import techpriest.Url_Shortener.exceptions.UnauthorizedException;
import techpriest.Url_Shortener.services.JwtService;

@Component
public class JwtAuthInterceptor implements HandlerInterceptor {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;

    public JwtAuthInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            throw new UnauthorizedException("Missing or malformed Authorization header");
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        try {
            String userId = jwtService.validateAccessTokenAndGetUserId(token);
            request.setAttribute("userId", userId);
        } catch (JwtException e) {
            throw new UnauthorizedException("Invalid or expired token");
        }

        return true; // allow the request to proceed to the controller
    }
}
