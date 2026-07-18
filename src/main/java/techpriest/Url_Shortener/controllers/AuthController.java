package techpriest.Url_Shortener.controllers;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import techpriest.Url_Shortener.dto.AuthResponse;
import techpriest.Url_Shortener.dto.LoginDto;
import techpriest.Url_Shortener.dto.RefreshTokenDto;
import techpriest.Url_Shortener.dto.TokenPair;
import techpriest.Url_Shortener.services.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(authService.login(loginDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenPair> refresh(@Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        return ResponseEntity.ok(authService.refresh(refreshTokenDto));
    }
}
