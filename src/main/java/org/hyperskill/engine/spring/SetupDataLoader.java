package org.hyperskill.engine.spring;

import java.util.*;

import org.hyperskill.engine.persistence.dao.PrivilegeRepository;
import org.hyperskill.engine.persistence.dao.QuizRepository;
import org.hyperskill.engine.persistence.dao.RoleRepository;
import org.hyperskill.engine.persistence.dao.UserRepository;
import org.hyperskill.engine.persistence.model.Privilege;
import org.hyperskill.engine.persistence.model.Quiz;
import org.hyperskill.engine.persistence.model.Role;
import org.hyperskill.engine.persistence.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final QuizRepository quizRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SetupDataLoader(UserRepository userRepository, RoleRepository roleRepository, PrivilegeRepository privilegeRepository, QuizRepository quizRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.quizRepository = quizRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // API

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        // == create initial privileges
        final Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        final Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
        final Privilege passwordPrivilege = createPrivilegeIfNotFound("CHANGE_PASSWORD_PRIVILEGE");

        // == create initial roles
        final List<Privilege> adminPrivileges = new ArrayList<>(Arrays.asList(readPrivilege, writePrivilege, passwordPrivilege));
        final List<Privilege> userPrivileges = new ArrayList<>(Arrays.asList(readPrivilege, passwordPrivilege));
        final Role adminRole = createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        final Role userRole = createRoleIfNotFound("ROLE_USER", userPrivileges);

        // == create initial user
        User adminUser = createUserIfNotFound("test@test.com", "password", new ArrayList<>(Arrays.asList(adminRole)));

        // == create initial quizzes
        final List<Quiz> quizzes = new ArrayList<>(Arrays.asList(
                createQuizIfNotFound("The Java Logo",
                        "What is depicted on the Java logo?",
                        new String[]{"Robot","Tea leaf","Cup of coffee","Bug"},
                        new int[]{2},
                        adminUser),
                createQuizIfNotFound("The Ultimate Question",
                        "What is the answer to the Ultimate Question of Life, the Universe and Everything?",
                        new String[]{"Everything goes right","42","2+2=4","11011100"},
                        new int[]{1},
                        adminUser),
                createQuizIfNotFound("Math1",
                        "Which of the following is equal to 4?",
                        new String[]{"1+3","2+2","8-1","1+5"},
                        new int[]{0,1},
                        adminUser),
                createQuizIfNotFound("Math2",
                        "Which of the following is equal to 4?",
                        new String[]{"1+1","2+2","8-1","5-1"},
                        new int[]{1,3},
                        adminUser),
                createQuizIfNotFound("Math5",
                        "Which of the following is equal to 4?",
                        new String[]{"2^2","2+2","2-2","2*2"},
                        new int[]{0,1,3},
                        adminUser)
        ));

        alreadySetup = true;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(final String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilege = privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(final String name, final Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
        }
        role.setPrivileges(privileges);
        role = roleRepository.save(role);
        return role;
    }

    @Transactional
    User createUserIfNotFound(final String email, final String password, final Collection<Role> roles) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setPassword(passwordEncoder.encode(password));
            user.setEmail(email);
            user.setEnabled(true);
        }
        user.setRoles(roles);
        user = userRepository.save(user);
        return user;
    }

    @Transactional
    Quiz createQuizIfNotFound(final String title, final String text, final String[] options,
                              final int[] answer, final User user) {
        Quiz quiz = quizRepository.findByTitle(title);
        if (quiz == null) {
            quiz = new Quiz();
            quiz.setTitle(title);
            quiz.setText(text);
            quiz.setOptions(options);
            quiz.setAnswer(answer);
            quiz.setAuthor(user);
            quizRepository.save(quiz);
        }
        return quiz;
    }
}