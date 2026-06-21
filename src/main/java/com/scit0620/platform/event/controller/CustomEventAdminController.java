package com.scit0620.platform.event.controller;

import com.scit0620.platform.auth.security.CurrentUser;
import com.scit0620.platform.event.dto.EventUpdateRequest;
import com.scit0620.platform.event.service.CustomEventAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/events")
public class CustomEventAdminController {

	private final CustomEventAdminService customEventAdminService;

	public CustomEventAdminController(CustomEventAdminService customEventAdminService) {
		this.customEventAdminService = customEventAdminService;
	}

	@DeleteMapping("/{eventId}")
	public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
		customEventAdminService.deleteEvent(eventId, CurrentUser.get());
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{eventId}")
	public ResponseEntity<Void> updateEvent(@PathVariable Long eventId, @RequestBody EventUpdateRequest request) {
		customEventAdminService.updateEvent(eventId, request, CurrentUser.get());
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{eventId}/feedbacks")
	public ResponseEntity<List<Map<String, Object>>> getFeedbacks(@PathVariable Long eventId) {
		return ResponseEntity.ok(customEventAdminService.getFeedbacks(eventId, CurrentUser.get()));
	}

	@GetMapping("/{eventId}/attendances")
	public ResponseEntity<List<Map<String, Object>>> getAttendances(@PathVariable Long eventId) {
		return ResponseEntity.ok(customEventAdminService.getAttendances(eventId, CurrentUser.get()));
	}
}
