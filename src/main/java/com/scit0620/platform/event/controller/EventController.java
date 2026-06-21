package com.scit0620.platform.event.controller;

import com.scit0620.platform.auth.security.CurrentUser;
import com.scit0620.platform.event.dto.AttendanceCodeResponse;
import com.scit0620.platform.event.dto.EventCreateRequest;
import com.scit0620.platform.event.dto.EventResponse;
import com.scit0620.platform.event.dto.EventStatusUpdateRequest;
import com.scit0620.platform.event.service.EventService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
public class EventController {

	private final EventService eventService;

	public EventController(EventService eventService) {
		this.eventService = eventService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EventResponse createEvent(@RequestBody EventCreateRequest request) {
		return eventService.createEvent(request, CurrentUser.get());
	}

	@GetMapping
	public List<EventResponse> getEvents() {
		return eventService.getEvents();
	}

	@GetMapping("/{eventId}")
	public EventResponse getEvent(@PathVariable Long eventId) {
		return eventService.getEvent(eventId);
	}

	@PatchMapping("/{eventId}/status")
	public EventResponse updateStatus(@PathVariable Long eventId, @RequestBody EventStatusUpdateRequest request) {
		return eventService.updateStatus(eventId, request, CurrentUser.get());
	}

	@PostMapping("/{eventId}/attendance-code")
	public AttendanceCodeResponse issueAttendanceCode(@PathVariable Long eventId) {
		return eventService.issueAttendanceCode(eventId, CurrentUser.get());
	}
}
