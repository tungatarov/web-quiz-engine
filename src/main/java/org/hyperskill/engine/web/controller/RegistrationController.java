package org.hyperskill.engine.web.controller;

import org.hyperskill.engine.persistence.dao.UserRepository;
import org.hyperskill.engine.persistence.model.User;
import org.hyperskill.engine.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class RegistrationController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public RegistrationController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/api/register")
    public ResponseEntity<User> registerUserAccount(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.registerNewUserAccount(user));
    }
}
