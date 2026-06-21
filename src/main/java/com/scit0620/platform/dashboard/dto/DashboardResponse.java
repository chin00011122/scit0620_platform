package com.scit0620.platform.dashboard.dto;

import java.util.List;

public record DashboardResponse(
		long totalEvents,
		long openEvents,
		long totalApplications,
		long approvedApplications,
		long rejectedApplications,
		long totalAttendances,
		long totalFeedbacks,
		List<EventDashboardItem> events
) {
}
