package org.hyperskill.engine.service;

import org.hyperskill.engine.persistence.dao.RoleRepository;
import org.hyperskill.engine.persistence.dao.UserRepository;
import org.hyperskill.engine.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@Component
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public User registerNewUserAccount(User accountDto) {
        if (emailExists(accountDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is an account with that email address: " + accountDto.getEmail());
        }
        User user = new User();
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        user.setEmail(accountDto.getEmail());
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private boolean emailExists(final String email) {
        return userRepository.findByEmail(email) != null;
    }
}