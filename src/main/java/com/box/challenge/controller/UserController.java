package com.box.challenge.controller;

import com.box.challenge.controller.dto.ApiResponse;
import com.box.challenge.model.User;
import com.box.challenge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "users", name = "UserController", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController extends GenericController<User> {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Autowired
    UserController(UserService userService, PasswordEncoder passwordEncoder) {
        super(userService);
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        }

        if (userService.findByUsername(user.getUsername()) != null) {
            return new ResponseEntity<>(new ApiResponse(false, "Usuario ya existe!"), HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        User newUser = this.userService.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @Override
    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        }

        User userPersist = this.userService.findById(user.getId());
        user.setPassword(userPersist.getPassword());

        User newUser = this.userService.save(user);

        if (newUser == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error al actualizar");
        }
        return ResponseEntity.ok().body(newUser);

    }

    @Override
    @GetMapping
    public ResponseEntity<Page<User>> list(Pageable pageable) {
        return ResponseEntity.ok(this.userService.findAll(pageable));
    }

}
