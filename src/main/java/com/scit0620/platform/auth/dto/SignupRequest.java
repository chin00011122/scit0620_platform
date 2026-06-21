package com.scit0620.platform.auth.dto;

import com.scit0620.platform.user.domain.Role;

public record SignupRequest(
		String name,
		String email,
		String password,
		Role role
) {
}
