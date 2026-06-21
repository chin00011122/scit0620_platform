package com.scit0620.platform.event.dto;

import java.time.LocalDateTime;

public record EventCreateRequest(
		String title,
		String description,
		String location,
		LocalDateTime eventDateTime,
		int capacity
) {
}
