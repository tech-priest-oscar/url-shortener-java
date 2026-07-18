package techpriest.Url_Shortener.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResendOtpDto {

    @NotBlank
    @Email(message = "Email should be valid")
    private String email;

}
