package com.box.challenge.controller.dto;

import com.box.challenge.model.User;

public class LoginRequest extends User {

	public LoginRequest() {
	}

	public LoginRequest(String username, String password) {
		super(username, password);
	}

}