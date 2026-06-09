package techpriest.Url_Shortener.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import techpriest.Url_Shortener.models.UserRole;
import techpriest.Url_Shortener.models.UserStatus;
import jakarta.validation.constraints.NotNull;


@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CreateUserDto extends UserRegistrationDto {

    @NotNull
    private UserStatus status;

    @NotNull
    private UserRole role;
    
}
