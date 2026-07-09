package techpriest.Url_Shortener.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import techpriest.Url_Shortener.models.UserRole;
import techpriest.Url_Shortener.models.UserStatus;


@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CreateUserDto extends UserRegistrationDto {

    @NotNull
    private UserStatus status;

    @NotNull
    private UserRole role;
    
}
