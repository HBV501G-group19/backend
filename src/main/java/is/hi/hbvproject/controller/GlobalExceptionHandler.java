package is.hi.hbvproject.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
    MethodArgumentNotValidException ex,
    HttpHeaders headers,
    HttpStatus status,
    WebRequest request
  ) {
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
    responseBody.put("status", status.value());
    String path = request.getDescription(false).replace("uri=", "");
    responseBody.put("path", path);

    return ResponseEntity
      .status(status)
      .body(responseBody);
  }

  @Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(
    HttpMessageNotReadableException ex, 
    HttpHeaders headers, 
    HttpStatus status, 
    WebRequest request
  ) {
    String msg = ex.getRootCause().getMessage();
    boolean isValidationError = msg.contains("not subtype of");
    if (isValidationError) {
      return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(makeErrorBody("validation", "error in some field in geojson, sorry for this bad message, java spring sucks", HttpStatus.BAD_REQUEST.value(), request));
    }

    return ResponseEntity
      .status(status)
      .body(makeErrorBody("parsing", "error reading request", status.value(), request));
  }

  @Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
      HttpMediaTypeNotSupportedException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request
  ) {
    return ResponseEntity
    .status(status)
    .body(makeErrorBody("headers", ex.getMessage(), status.value(), request));
  }
  
  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
    HttpMediaTypeNotAcceptableException ex, 
    HttpHeaders headers, 
    HttpStatus status, 
    WebRequest request
  ) {
    return ResponseEntity
    .status(status)
    .body(makeErrorBody("response", ex.getMessage(), status.value(), request));
  }

  @Override
  protected ResponseEntity<Object> handleMissingPathVariable(
    MissingPathVariableException ex, 
    HttpHeaders headers, 
    HttpStatus status, 
    WebRequest request
  ) {
    return ResponseEntity
    .status(status)
    .body(makeErrorBody("path", ex.getMessage(), status.value(), request));
  }

  @Override
  protected ResponseEntity<Object> handleAsyncRequestTimeoutException(
    AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatus status, WebRequest webRequest) {

  return handleExceptionInternal(ex, null, headers, status, webRequest);
  }

  @ExceptionHandler
  protected ResponseEntity<Object> handleSocketTimeoutException(SocketTimeoutException ex) {
    // gets thrown if backend can't reach ORS for example
    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(makeErrorBody("socket timeout", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
  }

  @ExceptionHandler
  protected ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
    return ResponseEntity
      .status(ex.getStatus())
      .body(makeErrorBody(ex.getReason(), ex.getReason(), ex.getStatus().value()));
  }

  @ExceptionHandler
  protected ResponseEntity<Object> handleValidationException(ConstraintViolationException ex) {
    Set<ConstraintViolation<?>> errors = ex.getConstraintViolations();
    List<Object> errorList = new ArrayList<>();
    for(ConstraintViolation<?> error : errors) {
      String[] message = error.getMessage().split(" :: ");

      Map<String, Object> errorObject = new HashMap<>();
      errorObject.put("field", message[0]);
      errorObject.put("message", message[1]);
      errorList.add(errorObject);
    }

    Map<String, Object> responseBody = new HashMap<>();
    responseBody.put("errors", errorList);
    responseBody.put("error", "validation");
    responseBody.put("status", HttpStatus.BAD_REQUEST.value());

    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(responseBody);
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
    Exception ex, 
    @Nullable Object body, 
    HttpHeaders headers, 
    HttpStatus status, 
    WebRequest request
  ) {
    if (status.value() < 500 || status.value() > 599) {
      Map<String, Object> responseBody = makeErrorBody("unknown", ex.getMessage(), status.value(), request);
      responseBody.put("shame", "someone should make an error handler for this...");
      return ResponseEntity
      .status(status)
      .body(responseBody);
    }

    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(makeErrorBody("internal", "internal server error", status.value(), request));
  }

  private Map<String, Object> makeErrorBody(String error, String message, int status) {
    return makeErrorBody(error, message, status, null);
  }
  
  private Map<String, Object> makeErrorBody(String error, String message, int status, @Nullable WebRequest request) {
    Map<String, Object> responseBody = new HashMap<>();
    responseBody.put("error", error);
    responseBody.put("message", message);
    responseBody.put("status", status);
    if (request != null) {
      String path = request.getDescription(false).replace("uri=", "");
      responseBody.put("path", path);
    }

    return responseBody;
  }
}
