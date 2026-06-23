package com.scit0620.platform.event.service;

import com.scit0620.platform.auth.security.UserPrincipal;
import com.scit0620.platform.event.dto.EventUpdateRequest;
import com.scit0620.platform.global.exception.BusinessException;
import com.scit0620.platform.user.domain.Role;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class CustomEventAdminService {

	private final JdbcTemplate jdbcTemplate;

	public CustomEventAdminService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Transactional
	public void deleteEvent(Long eventId, UserPrincipal principal) {
		if (principal.role() != Role.ADMIN && principal.role() != Role.STAFF) {
			throw new BusinessException(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");
		}
		
		jdbcTemplate.update("DELETE FROM feedbacks WHERE event_id = ?", eventId);
		jdbcTemplate.update("DELETE FROM attendances WHERE event_id = ?", eventId);
		jdbcTemplate.update("DELETE FROM event_applications WHERE event_id = ?", eventId);
		jdbcTemplate.update("DELETE FROM events WHERE id = ?", eventId);
	}

	@Transactional
	public void updateEvent(Long eventId, EventUpdateRequest request, UserPrincipal principal) {
		if (principal.role() != Role.ADMIN && principal.role() != Role.STAFF) {
			throw new BusinessException(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");
		}

		int rows = jdbcTemplate.update(
				"UPDATE events SET title = ?, description = ?, location = ?, event_date_time = ?, capacity = ? WHERE id = ?",
				request.title(),
				request.description(),
				request.location(),
				request.eventDateTime(),
				request.capacity(),
				eventId
		);

		if (rows == 0) {
			throw new BusinessException(HttpStatus.NOT_FOUND, "행사를 찾을 수 없습니다.");
		}
	}

	public List<Map<String, Object>> getFeedbacks(Long eventId, UserPrincipal principal) {
		if (principal.role() != Role.ADMIN && principal.role() != Role.STAFF) {
			throw new BusinessException(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");
		}
		return jdbcTemplate.queryForList(
			"SELECT f.id, f.rating, f.content, u.name as member_name " +
			"FROM feedbacks f JOIN users u ON f.member_id = u.id " +
			"WHERE f.event_id = ?", eventId);
	}

	public List<Map<String, Object>> getAttendances(Long eventId, UserPrincipal principal) {
		if (principal.role() != Role.ADMIN && principal.role() != Role.STAFF) {
			throw new BusinessException(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");
		}
		return jdbcTemplate.queryForList(
			"SELECT a.id, a.checked_in_at as check_in_time, u.name as member_name, u.email as member_email " +
			"FROM attendances a JOIN users u ON a.member_id = u.id " +
			"WHERE a.event_id = ?", eventId);
	}
}
