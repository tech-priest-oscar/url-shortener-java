package techpriest.Url_Shortener.controllers;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import techpriest.Url_Shortener.dto.UserRegistrationDto;
import techpriest.Url_Shortener.services.UserService;
import techpriest.Url_Shortener.services.OnboardingService;

@RestController
@RequestMapping("/api/onboarding")
public class OnboardingController {
    private final UserService userService;
    private final OnboardingService onboardingService;

    public OnboardingController(UserService userService, OnboardingService onboardingService) {
        this.userService = userService;
        this.onboardingService = onboardingService;
    }

    @PostMapping("/register")
    public void registerUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        onboardingService.registerUser(userRegistrationDto);
        
    }
    
}




