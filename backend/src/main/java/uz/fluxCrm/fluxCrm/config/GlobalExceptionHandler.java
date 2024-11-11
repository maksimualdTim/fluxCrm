package uz.fluxCrm.fluxCrm.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorValidationResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorDetail> errors = new ArrayList<>();
        
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();

            if ("statusId".equals(fieldName)) {
                fieldName = "status_id";  // Заменяем на нужный формат
            }

            String errorMessage = error.getDefaultMessage();
            errors.add(new ErrorDetail(fieldName, errorMessage));
        });
        ErrorValidationResponse errorResponse = new ErrorValidationResponse(HttpStatus.BAD_REQUEST.value(), errors);
        return new ResponseEntity<ErrorValidationResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    

    @Getter
    @Setter
    @AllArgsConstructor
    public class ErrorResponse {
        private int status;
        private String message;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    public class ErrorValidationResponse {
        private int status;
        private List<ErrorDetail> errors;
    }
    
    @Getter
    @Setter
    @AllArgsConstructor
    public class ErrorDetail {
        private String field;
        private String message;
    }
}
