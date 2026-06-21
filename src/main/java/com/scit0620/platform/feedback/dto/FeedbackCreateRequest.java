package com.scit0620.platform.feedback.dto;

public record FeedbackCreateRequest(
		int rating,
		String content
) {
}
