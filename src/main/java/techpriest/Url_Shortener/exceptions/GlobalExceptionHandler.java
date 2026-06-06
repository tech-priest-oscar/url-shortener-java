package techpriest.Url_Shortener.exceptions;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
        MethodArgumentNotValidException exception, HttpServletRequest request) {
            /* 
            this will only return the first error message for each field, 
            if there are multiple errors for the same field, only the first one will be returned 
            */

            Map<String, String> fieldErrors = new LinkedHashMap<>();
            for (FieldError error : exception.getBindingResult().getFieldErrors()) {
                fieldErrors.putIfAbsent(error.getField(), error.getDefaultMessage());
            }

            Map<String, Object> body = new LinkedHashMap<>();
            body.put("timestamp", Instant.now().toString());
            body.put("status", HttpStatus.BAD_REQUEST.value());
            body.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
            body.put("path", request.getRequestURI());
            body.put("errors", fieldErrors);

            return ResponseEntity.badRequest().body(body);


    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            NotFoundException exception, HttpServletRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", HttpStatus.NOT_FOUND.getReasonPhrase());
        body.put("message", exception.getMessage());
        body.put("path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
