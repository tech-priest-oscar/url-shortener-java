package techpriest.Url_Shortener.services.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import techpriest.Url_Shortener.dto.CreateUserDto;
import techpriest.Url_Shortener.dto.RegistrationResponse;
import techpriest.Url_Shortener.dto.UserRegistrationDto;
import techpriest.Url_Shortener.dto.UserResponseDto;
import techpriest.Url_Shortener.models.User;
import techpriest.Url_Shortener.models.UserRole;
import techpriest.Url_Shortener.models.UserStatus;
import techpriest.Url_Shortener.services.EmailService;
import techpriest.Url_Shortener.services.OnboardingService;
import techpriest.Url_Shortener.services.OtpService;
import techpriest.Url_Shortener.services.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class OnboardingServiceImpl implements OnboardingService {

    private final UserService userService;
    private final OtpService otpService;
    private final EmailService emailService;

    @Override
    public RegistrationResponse registerUser(UserRegistrationDto userRegistrationDto) {

        if (userService.emailExists(userRegistrationDto.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        CreateUserDto createUserDto = new CreateUserDto();

        // client-supplied fields (already validated at the controller)
        createUserDto.setFirstName(userRegistrationDto.getFirstName());
        createUserDto.setLastName(userRegistrationDto.getLastName());
        createUserDto.setEmail(userRegistrationDto.getEmail());
        createUserDto.setPassword(userRegistrationDto.getPassword());

        // server-decided fields — deliberately NOT accepted from the client
        createUserDto.setRole(UserRole.USER);
        createUserDto.setStatus(UserStatus.INACTIVE);

        User savedUser = userService.create(createUserDto);

        String otpCode = otpService.generateFor(savedUser);
        try {
            emailService.sendOtpEmail(savedUser.getEmail(), savedUser.getFirstName(), otpCode);
        } catch (RuntimeException e) {
            log.error("Could not send OTP email to {}", savedUser.getEmail(), e);
        }

        String message = "User registered successfully. Please check your email for the OTP to activate your account.";

        return new RegistrationResponse(UserResponseDto.from(savedUser), message);
    }

}
