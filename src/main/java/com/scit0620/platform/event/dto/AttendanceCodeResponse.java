package com.scit0620.platform.event.dto;

import java.time.LocalDateTime;

public record AttendanceCodeResponse(
		Long eventId,
		String attendanceCode,
		LocalDateTime expiresAt
) {
}
