package com.scit0620.platform.attendance.dto;

import com.scit0620.platform.attendance.domain.Attendance;
import java.time.LocalDateTime;

public record AttendanceResponse(
		Long id,
		Long eventId,
		String eventTitle,
		Long memberId,
		String memberName,
		LocalDateTime checkedInAt
) {

	public static AttendanceResponse from(Attendance attendance) {
		return new AttendanceResponse(
				attendance.getId(),
				attendance.getEvent().getId(),
				attendance.getEvent().getTitle(),
				attendance.getMember().getId(),
				attendance.getMember().getName(),
				attendance.getCheckedInAt()
		);
	}
}
