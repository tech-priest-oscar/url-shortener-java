package techpriest.Url_Shortener.services;

import techpriest.Url_Shortener.dto.RegistrationResponse;
import techpriest.Url_Shortener.dto.UserRegistrationDto;

public interface OnboardingService {
    RegistrationResponse registerUser(UserRegistrationDto userRegistrationDto);
}
