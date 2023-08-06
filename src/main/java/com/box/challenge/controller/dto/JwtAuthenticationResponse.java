package com.box.challenge.controller.dto;

import java.util.Calendar;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {
	private String accessToken, tokenType = "Bearer";
	private Calendar createdToken;
	private long userId;
	private String roleUser;
	private Long createdBy;

	public JwtAuthenticationResponse(String accessToken, int minutosVencimientoToken, long userId, String roleUser,
			Long createdBy) {
		this.accessToken = accessToken;
		this.createdToken = Calendar.getInstance();
		this.createdToken.add(Calendar.MINUTE, minutosVencimientoToken);
		this.userId = userId;
		this.roleUser = roleUser;
		this.createdBy = createdBy;
	}

}