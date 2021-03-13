package org.hyperskill.engine.web.controller;

import org.hyperskill.engine.persistence.dao.UserRepository;
import org.hyperskill.engine.persistence.model.CompletedQuiz;
import org.hyperskill.engine.persistence.model.Quiz;
import org.hyperskill.engine.service.QuizService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.hyperskill.engine.service.QuizService.Answer;
import static org.hyperskill.engine.service.QuizService.Response;


@RestController
@RequestMapping("/api/quizzes")
@Component
public class QuizController {

    private static final String NOT_FOUND_MESSAGE = "not found";

    private final QuizService quizService;

    @Autowired
    public QuizController(QuizService quizService, UserRepository userRepository) {
        this.quizService = quizService;
    }

    @PostMapping()
    public Quiz addQuiz(@Valid @RequestBody Quiz quiz) {
        return quizService.addQuiz(quiz);
    }

    @PostMapping(value = "/{id}/solve", consumes = "application/json")
    public Response solveQuiz(@PathVariable Long id, @RequestBody Answer answer) {
        return quizService.solveQuiz(id, answer);
    }

    @GetMapping("/{id}")
    public Quiz getQuiz(@PathVariable Long id) {
        return quizService.getQuizById(id);
    }

    @GetMapping()
    public Page<Quiz> getQuizzes(@RequestParam(defaultValue = "0") Integer page) {
        return quizService.getAllQuizzes(page);
    }

    @GetMapping("/completed")
    public Page<CompletedQuiz> getCompletedQuizzes(@RequestParam(defaultValue = "0") Integer page) {
        return quizService.getAllCompletedQuizzes(page);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserCreatedQuiz(@PathVariable Long id) {
        return quizService.deleteUserCreatedQuizById(id);
    }
}
