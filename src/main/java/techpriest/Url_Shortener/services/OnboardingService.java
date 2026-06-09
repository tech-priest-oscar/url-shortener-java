package techpriest.Url_Shortener.services;

import org.springframework.stereotype.Service;

import techpriest.Url_Shortener.dto.AuthResponse;
import techpriest.Url_Shortener.dto.CreateUserDto;
import techpriest.Url_Shortener.dto.TokenPair;
import techpriest.Url_Shortener.dto.UserRegistrationDto;
import techpriest.Url_Shortener.dto.UserResponseDto;
import techpriest.Url_Shortener.models.User;
import techpriest.Url_Shortener.models.UserRole;
import techpriest.Url_Shortener.models.UserStatus;

@Service
public class OnboardingService {

    private final UserService userService;
    private final JwtService jwtService;

    public OnboardingService(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    public AuthResponse registerUser(UserRegistrationDto userRegistrationDto) {

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
        createUserDto.setStatus(UserStatus.ACTIVE);

        User savedUser = userService.create(createUserDto);
        TokenPair tokens = jwtService.generateTokens(savedUser);

        return new AuthResponse(UserResponseDto.from(savedUser), tokens);
    }

}
