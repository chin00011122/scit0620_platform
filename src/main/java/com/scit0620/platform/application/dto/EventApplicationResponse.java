package com.scit0620.platform.application.dto;

import com.scit0620.platform.application.domain.ApplicationStatus;
import com.scit0620.platform.application.domain.EventApplication;
import java.time.LocalDateTime;

public record EventApplicationResponse(
		Long id,
		Long eventId,
		String eventTitle,
		Long memberId,
		String memberName,
		String memberEmail,
		ApplicationStatus status,
		LocalDateTime appliedAt,
		LocalDateTime decidedAt
) {

	public static EventApplicationResponse from(EventApplication application) {
		return new EventApplicationResponse(
				application.getId(),
				application.getEvent().getId(),
				application.getEvent().getTitle(),
				application.getMember().getId(),
				application.getMember().getName(),
				application.getMember().getEmail(),
				application.getStatus(),
				application.getAppliedAt(),
				application.getDecidedAt()
		);
	}
}
