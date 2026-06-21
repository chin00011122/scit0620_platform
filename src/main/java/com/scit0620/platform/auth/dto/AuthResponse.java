package com.scit0620.platform.auth.dto;

import com.scit0620.platform.user.domain.Role;

public record AuthResponse(
		String token,
		Long userId,
		String name,
		String email,
		Role role
) {
}
