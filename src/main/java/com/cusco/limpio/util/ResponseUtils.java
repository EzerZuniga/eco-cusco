package com.cusco.limpio.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Helper para construir respuestas API consistentes.
 */
public final class ResponseUtils {

	private ResponseUtils() {}

	private static Map<String, Object> baseBody(int status, String message) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", Instant.now().toString());
		body.put("status", status);
		if (message != null) body.put("message", message);
		return body;
	}

	public static ResponseEntity<Map<String, Object>> ok(Object data) {
		Map<String, Object> body = baseBody(HttpStatus.OK.value(), null);
		body.put("data", data);
		return ResponseEntity.ok(body);
	}

	public static ResponseEntity<Map<String, Object>> created(Object data) {
		Map<String, Object> body = baseBody(HttpStatus.CREATED.value(), null);
		body.put("data", data);
		return ResponseEntity.status(HttpStatus.CREATED).body(body);
	}

	public static ResponseEntity<Map<String, Object>> error(HttpStatus status, String message) {
		Map<String, Object> body = baseBody(status.value(), message);
		body.put("error", message);
		return ResponseEntity.status(status).body(body);
	}

	public static ResponseEntity<Map<String, Object>> badRequest(String message) {
		return error(HttpStatus.BAD_REQUEST, message);
	}

	public static ResponseEntity<Map<String, Object>> notFound(String message) {
		return error(HttpStatus.NOT_FOUND, message);
	}

	public static ResponseEntity<Map<String, Object>> unauthorized(String message) {
		return error(HttpStatus.UNAUTHORIZED, message);
	}
}

