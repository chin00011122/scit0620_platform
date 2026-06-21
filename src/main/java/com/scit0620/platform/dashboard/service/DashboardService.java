package com.scit0620.platform.dashboard.service;

import com.scit0620.platform.application.domain.ApplicationStatus;
import com.scit0620.platform.application.repository.EventApplicationRepository;
import com.scit0620.platform.attendance.repository.AttendanceRepository;
import com.scit0620.platform.auth.security.UserPrincipal;
import com.scit0620.platform.dashboard.dto.DashboardResponse;
import com.scit0620.platform.dashboard.dto.EventDashboardItem;
import com.scit0620.platform.event.domain.Event;
import com.scit0620.platform.event.domain.EventStatus;
import com.scit0620.platform.event.repository.EventRepository;
import com.scit0620.platform.feedback.repository.FeedbackRepository;
import com.scit0620.platform.global.exception.BusinessException;
import com.scit0620.platform.user.domain.Role;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {

	private final EventRepository eventRepository;
	private final EventApplicationRepository applicationRepository;
	private final AttendanceRepository attendanceRepository;
	private final FeedbackRepository feedbackRepository;

	public DashboardService(EventRepository eventRepository, EventApplicationRepository applicationRepository,
			AttendanceRepository attendanceRepository, FeedbackRepository feedbackRepository) {
		this.eventRepository = eventRepository;
		this.applicationRepository = applicationRepository;
		this.attendanceRepository = attendanceRepository;
		this.feedbackRepository = feedbackRepository;
	}

	@Transactional(readOnly = true)
	public DashboardResponse getDashboard(UserPrincipal principal) {
		if (principal.role() != Role.ADMIN) {
			throw new BusinessException(HttpStatus.FORBIDDEN, "관리자만 대시보드를 조회할 수 있습니다.");
		}

		var events = eventRepository.findAll();
		var eventItems = events.stream()
				.map(this::toEventDashboardItem)
				.toList();

		return new DashboardResponse(
				eventRepository.count(),
				eventRepository.findByStatus(EventStatus.OPEN).size(),
				applicationRepository.count(),
				applicationRepository.countByStatus(ApplicationStatus.APPROVED),
				applicationRepository.countByStatus(ApplicationStatus.REJECTED),
				attendanceRepository.count(),
				feedbackRepository.count(),
				eventItems
		);
	}

	private EventDashboardItem toEventDashboardItem(Event event) {
		return new EventDashboardItem(
				event.getId(),
				event.getTitle(),
				event.getStatus(),
				event.getCapacity(),
				applicationRepository.countByEvent(event),
				applicationRepository.countByEventAndStatus(event, ApplicationStatus.APPROVED),
				attendanceRepository.countByEvent(event),
				feedbackRepository.countByEvent(event)
		);
	}
}
