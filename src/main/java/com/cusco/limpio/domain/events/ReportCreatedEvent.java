package com.cusco.limpio.domain.events;

import java.time.Instant;

public record ReportCreatedEvent(
		Long reportId,
		Long userId,
		String title,
		String category,
		String district,
		Instant createdAt) {

	public ReportCreatedEvent {
		if (createdAt == null) {
			createdAt = Instant.now();
		}
	}

	public static ReportCreatedEvent of(Long reportId, Long userId, String title, String category, String district) {
		return new ReportCreatedEvent(reportId, userId, title, category, district, Instant.now());
	}
}
