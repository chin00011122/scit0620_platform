package com.scit0620.platform.application.controller;

import com.scit0620.platform.application.dto.EventApplicationResponse;
import com.scit0620.platform.application.service.EventApplicationService;
import com.scit0620.platform.auth.security.CurrentUser;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class EventApplicationController {

	private final EventApplicationService applicationService;

	public EventApplicationController(EventApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	@PostMapping("/events/{eventId}/applications")
	@ResponseStatus(HttpStatus.CREATED)
	public EventApplicationResponse apply(@PathVariable Long eventId) {
		return applicationService.apply(eventId, CurrentUser.get());
	}

	@GetMapping("/events/{eventId}/applications")
	public List<EventApplicationResponse> getApplications(@PathVariable Long eventId) {
		return applicationService.getApplications(eventId, CurrentUser.get());
	}

	@PatchMapping("/applications/{applicationId}/approve")
	public EventApplicationResponse approve(@PathVariable Long applicationId) {
		return applicationService.approve(applicationId, CurrentUser.get());
	}

	@PatchMapping("/applications/{applicationId}/reject")
	public EventApplicationResponse reject(@PathVariable Long applicationId) {
		return applicationService.reject(applicationId, CurrentUser.get());
	}
}
