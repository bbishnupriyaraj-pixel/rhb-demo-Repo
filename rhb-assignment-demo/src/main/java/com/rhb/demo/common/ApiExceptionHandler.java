package com.rhb.demo.common;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.rhb.demo.employee.EmployeeNotFoundException;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
	@ExceptionHandler(EmployeeNotFoundException.class)
	ResponseEntity<?> notFound(EmployeeNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(Map.of("timestamp", Instant.now().toString(), "error", e.getMessage()));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	ResponseEntity<?> badRequest(IllegalArgumentException e) {
		return ResponseEntity.badRequest().body(Map.of("timestamp", Instant.now().toString(), "error", e.getMessage()));
	}
}
