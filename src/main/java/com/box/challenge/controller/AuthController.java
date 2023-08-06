package com.box.challenge.controller;

import com.box.challenge.controller.dto.ApiResponse;
import com.box.challenge.controller.dto.JwtAuthenticationResponse;
import com.box.challenge.controller.dto.LoginRequest;
import com.box.challenge.security.JwtTokenProvider;
import com.box.challenge.security.UserPrincipal;
import com.box.challenge.service.RoleService;
import com.box.challenge.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/authentication")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserService appUserService;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider tokenProvider;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public AuthController(AuthenticationManager authenticationManager, UserService appUserService,
                          RoleService roleService, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.appUserService = appUserService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        UserPrincipal userLogin = (UserPrincipal) authentication.getPrincipal();
        Long idUser = userLogin.getId();
        Long createdBy = userLogin.getCreatedBy();

        Collection<? extends GrantedAuthority> authority = userLogin.getAuthorities();
        String roles = authority.toString().replaceAll("[\\[\\](){}]", "").replaceAll(" ", "");

        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, jwtExpirationInMs, idUser, roles, createdBy));

    }


    @PostMapping("/logout")
    public ResponseEntity<?> logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SecurityContextHolder.clearContext();
        if (auth != null) {
            auth.setAuthenticated(false);
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.ok().body(new ApiResponse(true, "User logout successfully"));
    }


}