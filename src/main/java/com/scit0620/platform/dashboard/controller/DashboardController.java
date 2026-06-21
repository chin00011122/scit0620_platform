package com.scit0620.platform.dashboard.controller;

import com.scit0620.platform.auth.security.CurrentUser;
import com.scit0620.platform.dashboard.dto.DashboardResponse;
import com.scit0620.platform.dashboard.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {

	private final DashboardService dashboardService;

	public DashboardController(DashboardService dashboardService) {
		this.dashboardService = dashboardService;
	}

	@GetMapping("/dashboard")
	public DashboardResponse getDashboard() {
		return dashboardService.getDashboard(CurrentUser.get());
	}
}
