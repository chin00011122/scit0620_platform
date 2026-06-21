package com.scit0620.platform.attendance.controller;

import com.scit0620.platform.attendance.dto.AttendanceCheckInRequest;
import com.scit0620.platform.attendance.dto.AttendanceResponse;
import com.scit0620.platform.attendance.service.AttendanceService;
import com.scit0620.platform.auth.security.CurrentUser;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AttendanceController {

	private final AttendanceService attendanceService;

	public AttendanceController(AttendanceService attendanceService) {
		this.attendanceService = attendanceService;
	}

	@PostMapping("/events/{eventId}/attendance")
	@ResponseStatus(HttpStatus.CREATED)
	public AttendanceResponse checkIn(@PathVariable Long eventId, @RequestBody AttendanceCheckInRequest request) {
		return attendanceService.checkIn(eventId, request, CurrentUser.get());
	}
}
