package techpriest.Url_Shortener.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VerifyOtpDto {

    @NotBlank
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank
    @Pattern(regexp = "^\\d{5}$", message = "OTP must be exactly 5 digits")
    private String otpCode;

}
