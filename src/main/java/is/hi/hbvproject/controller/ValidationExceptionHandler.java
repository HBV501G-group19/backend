package is.hi.hbvproject.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ValidationExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
      List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
      List<Map<String, Object>> errors = new ArrayList<>();
      for(FieldError fieldError : fieldErrors) {
        Map<String, Object> error = new HashMap<>();
        error.put("field", fieldError.getField());
        error.put("message", fieldError.getDefaultMessage());

        errors.add(error);
      }
      
      Map<String, Object> responseBody = new HashMap<>();
      responseBody.put("errors", errors);
      return ResponseEntity.badRequest().body(responseBody);
    }
}
