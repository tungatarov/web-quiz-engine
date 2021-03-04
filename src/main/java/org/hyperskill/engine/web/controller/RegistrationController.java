package org.hyperskill.engine.web.controller;

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

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/register")
    public ResponseEntity<User> registerUserAccount(@Valid @RequestBody User user) {
        User user1 = userService.registerNewUserAccount(user);
        return ResponseEntity.ok(user1);
    }
}
