package com.scit0620.platform.global.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {

	private final HttpStatus status;

	public BusinessException(HttpStatus status, String message) {
		super(message);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}
}
