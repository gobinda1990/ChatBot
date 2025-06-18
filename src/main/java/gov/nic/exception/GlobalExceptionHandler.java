package gov.nic.exception;

import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import gov.nic.model.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiResponse<String>> handleIllegalArgument(IllegalArgumentException ex) {
	    logger.warn("Invalid argument: {}", ex.getMessage());

	    String message = (ex.getMessage() != null && !ex.getMessage().isBlank())
	            ? ex.getMessage()
	            : "The request contains invalid or missing parameters. Please check your input and try again.";

	    return ResponseEntity.badRequest()
	            .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), message, null));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<String>> handleValidationErrors(MethodArgumentNotValidException ex) {
	    String errorMsg = ex.getBindingResult().getFieldErrors().stream()
	            .map(error -> error.getField() + ": " + error.getDefaultMessage())
	            .collect(Collectors.joining(", "));

	    String message = "Some fields have invalid or missing values. Please correct the following: " + errorMsg;

	    logger.warn("Validation error: {}", message);

	    return ResponseEntity.badRequest()
	            .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), message, null));
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ApiResponse<String>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {

		logger.warn("Method not supported: {}. Supported methods: {}", ex.getMethod(), ex.getSupportedHttpMethods());
		String supportedMethods = ex.getSupportedHttpMethods() != null
				? ex.getSupportedHttpMethods().stream().map(HttpMethod::name).collect(Collectors.joining(", "))
				: "N/A";
		String message = "Request method '" + ex.getMethod() + "' not supported." + " Supported methods: "
				+ supportedMethods;
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
				.body(new ApiResponse<>(HttpStatus.METHOD_NOT_ALLOWED.value(), message, null));
	}

	
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<ApiResponse<String>> handleNoHandler(NoHandlerFoundException ex) {
		
	    logger.warn("No handler found for request URL: {}", ex.getRequestURL());
	    String message = "The page or service you are trying to access does not exist. Please check the URL and try again.";
	    return ResponseEntity.status(HttpStatus.NOT_FOUND)
	            .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), message, null));
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse<String>> handleResourceNotFound(ResourceNotFoundException ex) {
	    logger.warn("Resource not found: {}", ex.getMessage());

	    String message = "The requested resource could not be found. Please check the input and try again.";

	    return ResponseEntity.status(HttpStatus.NOT_FOUND)
	            .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), message, null));
	}
	
	@ExceptionHandler({ DataAccessException.class, DatabaseException.class })
	public ResponseEntity<ApiResponse<String>> handleDatabaseError(Exception ex) {
		
	    logger.error("Database exception occurred", ex.getMessage());
	    String message = "We're experiencing some technical difficulties accessing the database. "
	                   + "Please try again later or contact support if the issue persists.";
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, null));
	}
	
	@ExceptionHandler(EncryptionException.class)
	public ResponseEntity<ApiResponse<String>> handleEncryption(EncryptionException ex) {
		
	    logger.error("Encryption error occurred: {}", ex.getMessage(), ex.getMessage());
	    String message = "We encountered a problem while securing your data. "
	                   + "Please try again or contact support if the problem continues.";
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, null));
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<String>> handleAll(Exception ex) {
		
	    logger.error("Unhandled error occurred", ex.getMessage());
	    String message = "Something went wrong on our end. Please try again later or contact support if the issue persists.";
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, null));
	}
	
}
