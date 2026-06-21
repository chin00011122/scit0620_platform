package com.scit0620.platform.feedback.controller;

import com.scit0620.platform.auth.security.CurrentUser;
import com.scit0620.platform.feedback.dto.FeedbackCreateRequest;
import com.scit0620.platform.feedback.dto.FeedbackResponse;
import com.scit0620.platform.feedback.service.FeedbackService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeedbackController {

	private final FeedbackService feedbackService;

	public FeedbackController(FeedbackService feedbackService) {
		this.feedbackService = feedbackService;
	}

	@PostMapping("/events/{eventId}/feedback")
	@ResponseStatus(HttpStatus.CREATED)
	public FeedbackResponse createFeedback(@PathVariable Long eventId, @RequestBody FeedbackCreateRequest request) {
		return feedbackService.createFeedback(eventId, request, CurrentUser.get());
	}
}
