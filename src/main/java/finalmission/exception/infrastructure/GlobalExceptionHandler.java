package finalmission.exception.infrastructure;

import finalmission.exception.domain.CustomException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> nonExpectedExceptionHandler(final Exception exception) {
        exception.printStackTrace();
        return new ResponseEntity<>("예상하지 못한 예외가 발생하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleCustomException(final CustomException exception) {
        exception.printStackTrace();
        return new ResponseEntity<>(exception.getMessage(), exception.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            final MethodArgumentNotValidException exception) {
        exception.printStackTrace();
        final Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
