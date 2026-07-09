package techpriest.Url_Shortener.services;

import techpriest.Url_Shortener.dto.AuthResponse;
import techpriest.Url_Shortener.dto.UserRegistrationDto;

public interface OnboardingService {
    AuthResponse registerUser(UserRegistrationDto userRegistrationDto);
}
