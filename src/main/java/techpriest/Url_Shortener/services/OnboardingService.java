package techpriest.Url_Shortener.services;

import techpriest.Url_Shortener.dto.AuthResponse;
import techpriest.Url_Shortener.dto.MessageResponse;
import techpriest.Url_Shortener.dto.RegistrationResponse;
import techpriest.Url_Shortener.dto.ResendOtpDto;
import techpriest.Url_Shortener.dto.UserRegistrationDto;
import techpriest.Url_Shortener.dto.VerifyOtpDto;

public interface OnboardingService {
    RegistrationResponse registerUser(UserRegistrationDto userRegistrationDto);

    /** Validates the OTP; on success activates the account and logs the user in. */
    AuthResponse verifyEmail(VerifyOtpDto verifyOtpDto);

    /** Issues a fresh OTP (invalidating any previous one) and emails it. */
    MessageResponse resendOtp(ResendOtpDto resendOtpDto);
}
