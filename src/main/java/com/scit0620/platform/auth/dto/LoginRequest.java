package com.scit0620.platform.auth.dto;

public record LoginRequest(
		String email,
		String password
) {
}
