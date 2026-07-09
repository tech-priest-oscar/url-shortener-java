package techpriest.Url_Shortener.services;

import techpriest.Url_Shortener.dto.AuthResponse;
import techpriest.Url_Shortener.dto.LoginDto;
import techpriest.Url_Shortener.dto.RefreshTokenDto;
import techpriest.Url_Shortener.dto.TokenPair;

public interface AuthService {
    AuthResponse login(LoginDto loginDto);
    TokenPair refresh(RefreshTokenDto refreshTokenDto);
}
