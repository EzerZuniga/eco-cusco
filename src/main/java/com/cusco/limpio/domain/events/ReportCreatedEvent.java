package com.cusco.limpio.domain.events;

import java.time.Instant;

/**
 * Evento de dominio que representa la creación de un reporte.
 * Inmutable y ligero, pensado para envío a colas o publicación interna.
 */
public record ReportCreatedEvent(
		Long reportId,
		Long userId,
		String title,
		String category,
		String district,
		Instant createdAt
) {

	public ReportCreatedEvent {
		if (createdAt == null) {
			createdAt = Instant.now();
		}
	}

	public static ReportCreatedEvent of(Long reportId, Long userId, String title, String category, String district) {
		return new ReportCreatedEvent(reportId, userId, title, category, district, Instant.now());
	}
}
