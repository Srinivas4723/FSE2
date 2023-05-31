package com.blogsite.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

public class ErrorController {

	public ErrorController() {
		super();
	}
	
	/**
	 * Invalid Argument
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	Map<String, String> handleException(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<String, String>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldname = ((FieldError) error).getField();
			String message = ((FieldError) error).getDefaultMessage();
			errors.put(fieldname, message);
		});
		return errors;
	}
	
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	ResponseEntity<String> RequestParamNotFound(HttpMessageNotReadableException ex) {
		return ResponseEntity.badRequest().body("Error : Request Body is missing ");
	}
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	ResponseEntity<String> typeMismatch(MethodArgumentTypeMismatchException ex) {
		return ResponseEntity.badRequest().body("Error : Invalid "+ex.getName());
	}
	
}