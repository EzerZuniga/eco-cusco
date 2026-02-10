package com.cusco.limpio.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public final class DateUtils {

	private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_DATE_TIME;

	private DateUtils() {}

	public static String toIso(LocalDateTime dateTime) {
		if (dateTime == null) return null;
		return dateTime.format(ISO);
	}

	public static LocalDateTime fromIso(String iso) {
		if (iso == null || iso.isBlank()) return null;
		try {
			return LocalDateTime.parse(iso, ISO);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("Invalid ISO date-time: " + iso, e);
		}
	}

	public static String nowIso() {
		return LocalDateTime.now().format(ISO);
	}

	public static String format(LocalDateTime dateTime, String pattern) {
		Objects.requireNonNull(pattern, "pattern must not be null");
		if (dateTime == null) return null;
		return dateTime.format(DateTimeFormatter.ofPattern(pattern));
	}
}

