package techpriest.Url_Shortener.controllers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import techpriest.Url_Shortener.dto.AuthResponse;
import techpriest.Url_Shortener.dto.MessageResponse;
import techpriest.Url_Shortener.dto.RegistrationResponse;
import techpriest.Url_Shortener.dto.ResendOtpDto;
import techpriest.Url_Shortener.dto.UserRegistrationDto;
import techpriest.Url_Shortener.dto.VerifyOtpDto;
import techpriest.Url_Shortener.services.OnboardingService;

@RestController
@RequestMapping("/api/onboarding")
public class OnboardingController {
    private final OnboardingService onboardingService;

    public OnboardingController(OnboardingService onboardingService) {
        this.onboardingService = onboardingService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> registerUser(
            @Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        RegistrationResponse response = onboardingService.registerUser(userRegistrationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<AuthResponse> verifyEmail(
            @Valid @RequestBody VerifyOtpDto verifyOtpDto) {
        AuthResponse response = onboardingService.verifyEmail(verifyOtpDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<MessageResponse> resendOtp(
            @Valid @RequestBody ResendOtpDto resendOtpDto) {
        MessageResponse response = onboardingService.resendOtp(resendOtpDto);
        return ResponseEntity.ok(response);
    }

}
