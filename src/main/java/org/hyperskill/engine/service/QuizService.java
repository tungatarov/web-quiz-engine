package org.hyperskill.engine.service;

import org.hyperskill.engine.persistence.dao.QuizRepository;
import org.hyperskill.engine.persistence.model.Quiz;
import org.hyperskill.engine.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class QuizService {

    private final QuizRepository quizRepository;
    private final UserService userService;
    private final HttpServletRequest request;

    @Autowired
    public QuizService(QuizRepository quizRepository, UserService userService, HttpServletRequest request) {
        this.quizRepository = quizRepository;
        this.userService = userService;
        this.request = request;
    }

    public Quiz addQuiz(Quiz quiz) {
        User user = userService.getUserByEmail(request.getUserPrincipal().getName());
        quiz.setUser(user);
        System.out.println(user.toString());

        return quizRepository.save(quiz);
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public Quiz getQuizById(Long id) {
        return quizRepository.findById(id).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<String> deleteUserCreatedQuizById(Long id) {
        User user = userService.getUserByEmail(request.getUserPrincipal().getName());
        Quiz quiz = getQuizById(id);
        if (!quiz.getUser().getId().equals(user.getId())) {
            throw  new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        quizRepository.delete(quiz);
        return ResponseEntity.noContent().build();
    }
}