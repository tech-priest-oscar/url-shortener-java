package techpriest.Url_Shortener.services;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtException;
import techpriest.Url_Shortener.dto.AuthResponse;
import techpriest.Url_Shortener.dto.LoginDto;
import techpriest.Url_Shortener.dto.RefreshTokenDto;
import techpriest.Url_Shortener.dto.TokenPair;
import techpriest.Url_Shortener.dto.UserResponseDto;
import techpriest.Url_Shortener.exceptions.UnauthorizedException;
import techpriest.Url_Shortener.models.User;
import techpriest.Url_Shortener.repositories.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
        UserRepository userRepository, 
        PasswordEncoder passwordEncoder,
        JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse login(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        TokenPair tokens = jwtService.generateTokens(user);
        return new AuthResponse(UserResponseDto.from(user), tokens);
    }

    public TokenPair refresh(RefreshTokenDto refreshTokenDto) {
        String userId;
        try {
            userId = jwtService.validateRefreshTokenAndGetUserId(refreshTokenDto.getRefreshToken());
        } catch (JwtException e) {
            throw new UnauthorizedException("Invalid or expired refresh token");
        }

        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new UnauthorizedException("User no longer exists"));

        // Rotate: issue a brand-new access + refresh pair.
        return jwtService.generateTokens(user);
    }
}
