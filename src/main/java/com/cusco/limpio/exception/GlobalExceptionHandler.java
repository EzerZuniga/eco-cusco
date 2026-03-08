package com.cusco.limpio.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	private Map<String, Object> buildBody(HttpStatus status, String message, String path) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", Instant.now().toString());
		body.put("status", status.value());
		body.put("error", status.getReasonPhrase());
		body.put("message", message);
		body.put("path", path);
		return body;
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex,
			HttpServletRequest request) {
		Map<String, Object> body = buildBody(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
		return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<Map<String, Object>> handleBadRequest(BadRequestException ex, HttpServletRequest request) {
		Map<String, Object> body = buildBody(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<Map<String, Object>> handleUnauthorized(UnauthorizedException ex,
			HttpServletRequest request) {
		Map<String, Object> body = buildBody(HttpStatus.UNAUTHORIZED, ex.getMessage(), request.getRequestURI());
		return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<Map<String, Object>> handleForbidden(ForbiddenException ex, HttpServletRequest request) {
		Map<String, Object> body = buildBody(HttpStatus.FORBIDDEN, ex.getMessage(), request.getRequestURI());
		return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex,
			HttpServletRequest request) {
		List<String> errors = ex.getBindingResult().getFieldErrors().stream()
				.map(FieldError::getDefaultMessage)
				.collect(Collectors.toList());

		Map<String, Object> body = buildBody(HttpStatus.BAD_REQUEST, "Validation failed", request.getRequestURI());
		body.put("validationErrors", errors);
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex,
			HttpServletRequest request) {
		Map<String, Object> body = buildBody(HttpStatus.FORBIDDEN, "No tienes permisos para realizar esta acción",
				request.getRequestURI());
		return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex,
			HttpServletRequest request) {
		Map<String, Object> body = buildBody(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleAll(Exception ex, HttpServletRequest request) {
		log.error("Unhandled exception at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
		Map<String, Object> body = buildBody(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error",
				request.getRequestURI());
		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
