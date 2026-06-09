package techpriest.Url_Shortener.dto;

import java.util.UUID;

import techpriest.Url_Shortener.models.User;
import techpriest.Url_Shortener.models.UserRole;
import techpriest.Url_Shortener.models.UserStatus;

/**
 * Public view of a user. Deliberately excludes the password hash.
 */
public record UserResponseDto(
        UUID id,
        String firstName,
        String lastName,
        String email,
        UserRole role,
        UserStatus status,
        boolean emailVerified) {

    public static UserResponseDto from(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.getStatus(),
                user.isEmailVerified());
    }
}
