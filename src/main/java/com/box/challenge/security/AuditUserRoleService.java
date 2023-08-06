package com.box.challenge.security;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuditUserRoleService {

	Authentication authentication;
	UserPrincipal userLogin;

	private void getUserLogged() {
		this.authentication = SecurityContextHolder.getContext().getAuthentication();
		// get logged in user
		this.userLogin = (UserPrincipal) authentication.getPrincipal();
	}

	public boolean userIdOk(long userId) {
		this.getUserLogged();
		// id user == id of the request
		return userLogin.getId() == userId;
	}

	public boolean isRoleSuperior() {
		this.getUserLogged();
		// control role of the request
		Collection<? extends GrantedAuthority> authority = userLogin.getAuthorities();

		for (GrantedAuthority grantedAuthority : authority) {
			if (grantedAuthority.getAuthority().compareTo("ROLE_SUPER_ADMIN") == 0
					|| grantedAuthority.getAuthority().compareTo("ROLE_DESARROLLADOR") == 0
					|| grantedAuthority.getAuthority().compareTo("ROLE_ADMIN") == 0) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isRoleDesarrollador() {
		this.getUserLogged();
		Collection<? extends GrantedAuthority> authority = userLogin.getAuthorities();

		for (GrantedAuthority grantedAuthority : authority) {
			if (grantedAuthority.getAuthority().compareTo("ROLE_DESARROLLADOR") == 0) {
				return true;
			}
		}
		return false;
	}

}
