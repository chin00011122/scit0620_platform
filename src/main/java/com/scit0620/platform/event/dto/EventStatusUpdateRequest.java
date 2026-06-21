package com.scit0620.platform.event.dto;

import com.scit0620.platform.event.domain.EventStatus;

public record EventStatusUpdateRequest(
		EventStatus status
) {
}
