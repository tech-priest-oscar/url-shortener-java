package techpriest.Url_Shortener.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRegistrationDto {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z]+$", message = "First name must contain only letters")
    private String firstName;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Last name must contain only letters")
    private String lastName;

    @NotBlank
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
    
}
