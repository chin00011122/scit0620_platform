package com.scit0620.platform.feedback.service;

import com.scit0620.platform.application.service.EventApplicationService;
import com.scit0620.platform.auth.security.UserPrincipal;
import com.scit0620.platform.event.domain.Event;
import com.scit0620.platform.event.service.EventService;
import com.scit0620.platform.feedback.domain.Feedback;
import com.scit0620.platform.feedback.dto.FeedbackCreateRequest;
import com.scit0620.platform.feedback.dto.FeedbackResponse;
import com.scit0620.platform.feedback.repository.FeedbackRepository;
import com.scit0620.platform.global.exception.BusinessException;
import com.scit0620.platform.user.domain.User;
import com.scit0620.platform.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedbackService {

	private final FeedbackRepository feedbackRepository;
	private final EventService eventService;
	private final EventApplicationService applicationService;
	private final UserRepository userRepository;

	public FeedbackService(FeedbackRepository feedbackRepository, EventService eventService,
			EventApplicationService applicationService, UserRepository userRepository) {
		this.feedbackRepository = feedbackRepository;
		this.eventService = eventService;
		this.applicationService = applicationService;
		this.userRepository = userRepository;
	}

	@Transactional
	public FeedbackResponse createFeedback(Long eventId, FeedbackCreateRequest request, UserPrincipal principal) {
		User member = getUser(principal.id());
		Event event = eventService.getEventEntity(eventId);

		applicationService.getApprovedApplication(event, member);
		if (request.rating() < 1 || request.rating() > 5) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "평점은 1점부터 5점까지 입력할 수 있습니다.");
		}
		if (feedbackRepository.existsByEventAndMember(event, member)) {
			throw new BusinessException(HttpStatus.CONFLICT, "이미 피드백을 작성한 행사입니다.");
		}

		Feedback feedback = new Feedback(event, member, request.rating(), request.content());
		return FeedbackResponse.from(feedbackRepository.save(feedback));
	}

	private User getUser(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
	}
}
