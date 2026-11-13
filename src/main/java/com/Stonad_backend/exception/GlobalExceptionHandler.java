package com.Stonad_backend.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationErrors(
	        MethodArgumentNotValidException ex) {

	    Map<String, String> errors = new HashMap<>();

	    ex.getBindingResult().getFieldErrors().forEach(error -> {
	        errors.put(error.getField(), error.getDefaultMessage());
	    });

	    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map<String, String>> handleConstraintViolation(
	        ConstraintViolationException ex) {

	    Map<String, String> errors = new HashMap<>();

	    ex.getConstraintViolations().forEach(error -> {
	        errors.put(
	            error.getPropertyPath().toString(),
	            error.getMessage()
	        );
	    });

	    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(DuplicateEmailException.class)
	public ResponseEntity<Map<String, String>> handleDuplicateEmail(
	        DuplicateEmailException ex) {

	    Map<String, String> errors = new HashMap<>();

	    errors.put("error", ex.getMessage());

	    return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(VisitorNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleVisitorNotFound(
	        VisitorNotFoundException ex) {

	    Map<String, String> errors = new HashMap<>();

	    errors.put("error", ex.getMessage());

	    return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler(InvalidPasswordException.class)
	public ResponseEntity<Map<String, String>> handleInvalidPassword(
	        InvalidPasswordException ex) {

	    Map<String, String> errors = new HashMap<>();

	    errors.put("error", ex.getMessage());

	    return new ResponseEntity<>(errors, HttpStatus.UNAUTHORIZED);
	}
	
	
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, String>> handleGlobalException(
	        Exception ex) {

	    Map<String, String> errors = new HashMap<>();

	    errors.put("error", "Something went wrong. Please try again later.");

	    return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}