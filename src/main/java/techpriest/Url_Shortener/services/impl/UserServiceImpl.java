package techpriest.Url_Shortener.services.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import techpriest.Url_Shortener.dto.CreateUserDto;
import techpriest.Url_Shortener.exceptions.NotFoundException;
import techpriest.Url_Shortener.models.User;
import techpriest.Url_Shortener.models.UserStatus;
import techpriest.Url_Shortener.repositories.UserRepository;
import techpriest.Url_Shortener.services.UserService;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User create(CreateUserDto createUserDto) {
        User user = User.builder()
                .firstName(createUserDto.getFirstName())
                .lastName(createUserDto.getLastName())
                .email(createUserDto.getEmail())
                .password(passwordEncoder.encode(createUserDto.getPassword()))
                .status(createUserDto.getStatus())
                .role(createUserDto.getRole())
                .build();
        return userRepository.save(user);
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("No account found for this email"));
    }

    @Override
    public User activate(User user) {
        user.setEmailVerified(true);
        user.setStatus(UserStatus.ACTIVE);
        return userRepository.save(user);
    }

}
