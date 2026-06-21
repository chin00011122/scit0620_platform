package com.scit0620.platform.dashboard.dto;

import com.scit0620.platform.event.domain.EventStatus;

public record EventDashboardItem(
		Long eventId,
		String title,
		EventStatus status,
		int capacity,
		long applications,
		long approvedApplications,
		long attendances,
		long feedbacks
) {
}
