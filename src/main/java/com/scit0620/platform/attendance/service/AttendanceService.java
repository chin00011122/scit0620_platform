package com.scit0620.platform.attendance.service;

import com.scit0620.platform.application.service.EventApplicationService;
import com.scit0620.platform.attendance.domain.Attendance;
import com.scit0620.platform.attendance.dto.AttendanceCheckInRequest;
import com.scit0620.platform.attendance.dto.AttendanceResponse;
import com.scit0620.platform.attendance.repository.AttendanceRepository;
import com.scit0620.platform.auth.security.UserPrincipal;
import com.scit0620.platform.event.domain.Event;
import com.scit0620.platform.event.service.EventService;
import com.scit0620.platform.global.exception.BusinessException;
import com.scit0620.platform.user.domain.User;
import com.scit0620.platform.user.repository.UserRepository;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AttendanceService {

	private final AttendanceRepository attendanceRepository;
	private final EventService eventService;
	private final EventApplicationService applicationService;
	private final UserRepository userRepository;

	public AttendanceService(AttendanceRepository attendanceRepository, EventService eventService,
			EventApplicationService applicationService, UserRepository userRepository) {
		this.attendanceRepository = attendanceRepository;
		this.eventService = eventService;
		this.applicationService = applicationService;
		this.userRepository = userRepository;
	}

	@Transactional
	public AttendanceResponse checkIn(Long eventId, AttendanceCheckInRequest request, UserPrincipal principal) {
		User member = getUser(principal.id());
		Event event = eventService.getEventEntity(eventId);

		applicationService.getApprovedApplication(event, member);
		if (!event.isAttendanceCodeValid(request.code(), LocalDateTime.now())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "출석 코드가 올바르지 않거나 만료되었습니다.");
		}
		if (attendanceRepository.existsByEventAndMember(event, member)) {
			throw new BusinessException(HttpStatus.CONFLICT, "이미 출석 처리된 행사입니다.");
		}

		Attendance attendance = new Attendance(event, member);
		return AttendanceResponse.from(attendanceRepository.save(attendance));
	}

	private User getUser(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
	}
}
