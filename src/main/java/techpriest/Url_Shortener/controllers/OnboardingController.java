package techpriest.Url_Shortener.controllers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import techpriest.Url_Shortener.dto.AuthResponse;
import techpriest.Url_Shortener.dto.UserRegistrationDto;
import techpriest.Url_Shortener.services.UserService;
import techpriest.Url_Shortener.services.OnboardingService;

@RestController
@RequestMapping("/api/onboarding")
public class OnboardingController {
    private final OnboardingService onboardingService;

    public OnboardingController(UserService userService, OnboardingService onboardingService) {
        this.onboardingService = onboardingService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(
            @Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        AuthResponse response = onboardingService.registerUser(userRegistrationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
}




