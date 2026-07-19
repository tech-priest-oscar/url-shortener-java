package techpriest.Url_Shortener.services.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import techpriest.Url_Shortener.dto.AuthResponse;
import techpriest.Url_Shortener.dto.CreateUserDto;
import techpriest.Url_Shortener.dto.MessageResponse;
import techpriest.Url_Shortener.dto.RegistrationResponse;
import techpriest.Url_Shortener.dto.ResendOtpDto;
import techpriest.Url_Shortener.dto.TokenPair;
import techpriest.Url_Shortener.dto.UserRegistrationDto;
import techpriest.Url_Shortener.dto.UserResponseDto;
import techpriest.Url_Shortener.dto.VerifyOtpDto;
import techpriest.Url_Shortener.models.User;
import techpriest.Url_Shortener.models.UserRole;
import techpriest.Url_Shortener.models.UserStatus;
import techpriest.Url_Shortener.services.EmailService;
import techpriest.Url_Shortener.services.JwtService;
import techpriest.Url_Shortener.services.OnboardingService;
import techpriest.Url_Shortener.services.OtpService;
import techpriest.Url_Shortener.services.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class OnboardingServiceImpl implements OnboardingService {

    private final UserService userService;
    private final JwtService jwtService;
    private final OtpService otpService;

    // For now, we'll be using the "javaMailSenderService" implementation of EmailService. 
    // In the future, we can add more implementations (e.g., SendGrid, AWS SES) and select 
    // them based on configuration or user preference.
    private final Map<String, EmailService> emailServices;

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
            EmailService emailService = emailServices.get("javaMailSenderService");
            emailService.sendOtpEmail(savedUser.getEmail(), savedUser.getFirstName(), otpCode);
        } catch (RuntimeException e) {
            log.error("Could not send OTP email to {}", savedUser.getEmail(), e);
        }

        String message = "User registered successfully. Please check your email for the OTP to activate your account.";

        return new RegistrationResponse(UserResponseDto.from(savedUser), message);
    }

    @Override
    public AuthResponse verifyEmail(VerifyOtpDto verifyOtpDto) {
        User user = userService.getByEmail(verifyOtpDto.getEmail());

        if (user.isEmailVerified()) {
            throw new IllegalArgumentException("Email is already verified");
        }

        if (!otpService.validate(user, verifyOtpDto.getOtpCode())) {
            throw new IllegalArgumentException("Invalid or expired OTP");
        }

        User activatedUser = userService.activate(user);
        TokenPair tokens = jwtService.generateTokens(activatedUser);

        return new AuthResponse(UserResponseDto.from(activatedUser), tokens);
    }

    @Override
    public MessageResponse resendOtp(ResendOtpDto resendOtpDto) {
        User user = userService.getByEmail(resendOtpDto.getEmail());

        if (user.isEmailVerified()) {
            throw new IllegalArgumentException("Email is already verified");
        }

        String otpCode = otpService.generateFor(user);
        EmailService emailService = emailServices.get("javaMailSenderService");
        emailService.sendOtpEmail(user.getEmail(), user.getFirstName(), otpCode);

        return new MessageResponse("A new OTP has been sent to your email.");
    }

}
