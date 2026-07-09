package techpriest.Url_Shortener.dto.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
@Data
@Builder
public class ResponseWrapper<T> {
    private T data;
    private String message;
    private HttpStatus httpStatus;
    private String errorCode;
}
