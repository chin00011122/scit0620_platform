package com.scit0620.platform.application.service;

import com.scit0620.platform.application.domain.ApplicationStatus;
import com.scit0620.platform.application.domain.EventApplication;
import com.scit0620.platform.application.dto.EventApplicationResponse;
import com.scit0620.platform.application.repository.EventApplicationRepository;
import com.scit0620.platform.auth.security.UserPrincipal;
import com.scit0620.platform.event.domain.Event;
import com.scit0620.platform.event.service.EventService;
import com.scit0620.platform.global.exception.BusinessException;
import com.scit0620.platform.user.domain.Role;
import com.scit0620.platform.user.domain.User;
import com.scit0620.platform.user.repository.UserRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventApplicationService {

	private final EventApplicationRepository applicationRepository;
	private final EventService eventService;
	private final UserRepository userRepository;

	public EventApplicationService(EventApplicationRepository applicationRepository, EventService eventService,
			UserRepository userRepository) {
		this.applicationRepository = applicationRepository;
		this.eventService = eventService;
		this.userRepository = userRepository;
	}

	@Transactional
	public EventApplicationResponse apply(Long eventId, UserPrincipal principal) {
		User member = getUser(principal.id());
		if (!member.hasRole(Role.MEMBER)) {
			throw new BusinessException(HttpStatus.FORBIDDEN, "일반 부원만 행사에 신청할 수 있습니다.");
		}

		Event event = eventService.getEventEntity(eventId);
		if (!event.isOpen()) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "모집중인 행사만 신청할 수 있습니다.");
		}
		if (applicationRepository.existsByEventAndMember(event, member)) {
			throw new BusinessException(HttpStatus.CONFLICT, "이미 신청한 행사입니다.");
		}

		EventApplication application = new EventApplication(event, member);
		return EventApplicationResponse.from(applicationRepository.save(application));
	}

	@Transactional(readOnly = true)
	public List<EventApplicationResponse> getApplications(Long eventId, UserPrincipal principal) {
		User user = getUser(principal.id());
		requireStaffOrAdmin(user);

		Event event = eventService.getEventEntity(eventId);
		return applicationRepository.findByEvent(event).stream()
				.map(EventApplicationResponse::from)
				.toList();
	}

	@Transactional
	public EventApplicationResponse approve(Long applicationId, UserPrincipal principal) {
		User user = getUser(principal.id());
		requireStaffOrAdmin(user);

		EventApplication application = getApplication(applicationId);
		requirePending(application);
		application.approve();
		return EventApplicationResponse.from(application);
	}

	@Transactional
	public EventApplicationResponse reject(Long applicationId, UserPrincipal principal) {
		User user = getUser(principal.id());
		requireStaffOrAdmin(user);

		EventApplication application = getApplication(applicationId);
		requirePending(application);
		application.reject();
		return EventApplicationResponse.from(application);
	}

	public EventApplication getApprovedApplication(Event event, User member) {
		EventApplication application = applicationRepository.findByEventAndMember(event, member)
				.orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "행사 신청 내역이 없습니다."));
		if (!application.isApproved()) {
			throw new BusinessException(HttpStatus.FORBIDDEN, "승인된 신청자만 처리할 수 있습니다.");
		}
		return application;
	}

	private EventApplication getApplication(Long applicationId) {
		return applicationRepository.findById(applicationId)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "신청 내역을 찾을 수 없습니다."));
	}

	private User getUser(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
	}

	private void requirePending(EventApplication application) {
		if (application.getStatus() != ApplicationStatus.PENDING) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "대기중인 신청만 승인 또는 반려할 수 있습니다.");
		}
	}

	private void requireStaffOrAdmin(User user) {
		if (!user.hasRole(Role.STAFF) && !user.hasRole(Role.ADMIN)) {
			throw new BusinessException(HttpStatus.FORBIDDEN, "운영진 이상만 처리할 수 있습니다.");
		}
	}
}
