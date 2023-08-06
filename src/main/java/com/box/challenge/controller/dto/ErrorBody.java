package com.box.challenge.controller.dto;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class ErrorBody {

	private String error;

	public ErrorBody() {
		super();
	}

	public ErrorBody(String error) {
		super();
		this.error = error;
	}

	public ErrorBody setError(String error) {
		this.error = error;
		return this;
	}

}
