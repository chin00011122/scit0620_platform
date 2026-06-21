package com.scit0620.platform.event.dto;

import com.scit0620.platform.event.domain.Event;
import com.scit0620.platform.event.domain.EventStatus;
import java.time.LocalDateTime;

public record EventResponse(
		Long id,
		String title,
		String description,
		String location,
		LocalDateTime eventDateTime,
		int capacity,
		EventStatus status,
		Long createdById,
		String createdByName,
		String attendanceCode,
		LocalDateTime attendanceCodeExpiresAt
) {

	public static EventResponse from(Event event) {
		return new EventResponse(
				event.getId(),
				event.getTitle(),
				event.getDescription(),
				event.getLocation(),
				event.getEventDateTime(),
				event.getCapacity(),
				event.getStatus(),
				event.getCreatedBy().getId(),
				event.getCreatedBy().getName(),
				event.getAttendanceCode(),
				event.getAttendanceCodeExpiresAt()
		);
	}
}
