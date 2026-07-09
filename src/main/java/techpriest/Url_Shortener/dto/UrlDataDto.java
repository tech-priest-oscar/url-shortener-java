package techpriest.Url_Shortener.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
public class UrlDataDto {

    @NotBlank
    @Size(min = 4, message = "Minimum url length: 4 characters")
    @Pattern(regexp = "^https?://.+", message = "URL must start with http:// or https://")
    private String url;
    
}
