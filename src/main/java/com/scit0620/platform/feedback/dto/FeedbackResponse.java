package com.scit0620.platform.feedback.dto;

import com.scit0620.platform.feedback.domain.Feedback;
import java.time.LocalDateTime;

public record FeedbackResponse(
		Long id,
		Long eventId,
		String eventTitle,
		Long memberId,
		String memberName,
		int rating,
		String content,
		LocalDateTime createdAt
) {

	public static FeedbackResponse from(Feedback feedback) {
		return new FeedbackResponse(
				feedback.getId(),
				feedback.getEvent().getId(),
				feedback.getEvent().getTitle(),
				feedback.getMember().getId(),
				feedback.getMember().getName(),
				feedback.getRating(),
				feedback.getContent(),
				feedback.getCreatedAt()
		);
	}
}
