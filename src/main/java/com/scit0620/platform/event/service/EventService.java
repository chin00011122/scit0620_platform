package com.scit0620.platform.event.service;

import com.scit0620.platform.auth.security.UserPrincipal;
import com.scit0620.platform.event.domain.Event;
import com.scit0620.platform.event.domain.EventStatus;
import com.scit0620.platform.event.dto.AttendanceCodeResponse;
import com.scit0620.platform.event.dto.EventCreateRequest;
import com.scit0620.platform.event.dto.EventResponse;
import com.scit0620.platform.event.dto.EventStatusUpdateRequest;
import com.scit0620.platform.event.repository.EventRepository;
import com.scit0620.platform.global.exception.BusinessException;
import com.scit0620.platform.user.domain.Role;
import com.scit0620.platform.user.domain.User;
import com.scit0620.platform.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {

	private final EventRepository eventRepository;
	private final UserRepository userRepository;

	public EventService(EventRepository eventRepository, UserRepository userRepository) {
		this.eventRepository = eventRepository;
		this.userRepository = userRepository;
	}

	@Transactional
	public EventResponse createEvent(EventCreateRequest request, UserPrincipal principal) {
		User creator = getUser(principal.id());
		requireAdmin(creator);

		Event event = new Event(
				request.title(),
				request.description(),
				request.location(),
				request.eventDateTime(),
				request.capacity(),
				creator
		);
		return EventResponse.from(eventRepository.save(event));
	}

	@Transactional(readOnly = true)
	public List<EventResponse> getEvents() {
		return eventRepository.findAll().stream()
				.map(EventResponse::from)
				.toList();
	}

	@Transactional(readOnly = true)
	public EventResponse getEvent(Long eventId) {
		return EventResponse.from(getEventEntity(eventId));
	}

	@Transactional
	public EventResponse updateStatus(Long eventId, EventStatusUpdateRequest request, UserPrincipal principal) {
		User user = getUser(principal.id());
		requireAdmin(user);

		Event event = getEventEntity(eventId);
		if (request.status() == EventStatus.OPEN) {
			event.open();
		} else if (request.status() == EventStatus.CLOSED) {
			event.close();
		} else if (request.status() == EventStatus.COMPLETED) {
			event.complete();
		} else {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "DRAFT 상태로 되돌릴 수 없습니다.");
		}

		return EventResponse.from(event);
	}

	@Transactional
	public AttendanceCodeResponse issueAttendanceCode(Long eventId, UserPrincipal principal) {
		User user = getUser(principal.id());
		requireStaffOrAdmin(user);

		Event event = getEventEntity(eventId);
		String code = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
		LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(30);
		event.issueAttendanceCode(code, expiresAt);
		return new AttendanceCodeResponse(event.getId(), code, expiresAt);
	}

	public Event getEventEntity(Long eventId) {
		return eventRepository.findById(eventId)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "행사를 찾을 수 없습니다."));
	}

	private User getUser(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
	}

	private void requireAdmin(User user) {
		if (!user.hasRole(Role.ADMIN)) {
			throw new BusinessException(HttpStatus.FORBIDDEN, "관리자만 처리할 수 있습니다.");
		}
	}

	private void requireStaffOrAdmin(User user) {
		if (!user.hasRole(Role.STAFF) && !user.hasRole(Role.ADMIN)) {
			throw new BusinessException(HttpStatus.FORBIDDEN, "운영진 이상만 처리할 수 있습니다.");
		}
	}
}
