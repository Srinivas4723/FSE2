package com.blogsite.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ExtendWith(MockitoExtension.class)
class ErrorControllerTest {
	
	@InjectMocks ErrorController errorController;
	//public MethodArgumentNotValidException methodArgumentNotValidException = mock(MethodArgumentNotValidException.class);
	//public BindingResult bindingResult = mock(BindingResult.class);
	@Test
	void testHandleException() {
			List<ObjectError> errors= new ArrayList<>();
			FieldError error = new FieldError("username","username","username cannot be blank");
			errors.add(error);
			BindingResult bindingResult = new BeanPropertyBindingResult("username", "objectName");
			bindingResult.addError(error);
			MethodArgumentNotValidException methodArgumentNotValidException= new MethodArgumentNotValidException(null, bindingResult);
			Map<String, String> x = errorController.handleException(methodArgumentNotValidException);
	        assertEquals("username cannot be blank",x.get("username"));
	}

	@Test
	void testRequestParamNotFound() {
		HttpMessageNotReadableException ex = mock(HttpMessageNotReadableException.class);
		assertEquals(ResponseEntity.badRequest().body("Error : Request Body is missing "),errorController.RequestParamNotFound(ex));
	}

	@Test
	void testTypeMismatch() {
		MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException(null, null, "name", null, null);
		assertEquals(ResponseEntity.badRequest().body("Error : Invalid "+ex.getName()),errorController.typeMismatch(ex));
	}

}
