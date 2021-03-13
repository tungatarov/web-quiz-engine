package org.hyperskill.engine.web.controller;

import org.hyperskill.engine.persistence.dao.UserRepository;
import org.hyperskill.engine.persistence.model.Quiz;
import org.hyperskill.engine.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;


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
        return answer.isEmpty() && getQuiz(id).isAnswerNull() || Arrays.equals(answer.getAnswer(), getQuiz(id).getAnswer())
                ? Response.CORRECT_ANSWER
                : Response.WRONG_ANSWER;
    }

    @GetMapping("/{id}")
    public Quiz getQuiz(@PathVariable Long id) {
        return quizService.getQuizById(id);
    }

    @GetMapping()
    public Page<Quiz> getQuizzes(@RequestParam(defaultValue = "0") Integer page) {
        return quizService.getAllQuizzes(page);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserCreatedQuiz(@PathVariable Long id) {
        return quizService.deleteUserCreatedQuizById(id);
    }


    static class Response {

        private final static Response CORRECT_ANSWER = new Response(true, "Congratulations, you're right!");
        private final static Response WRONG_ANSWER = new Response(false, "Wrong answer! Please, try again.");

        private final boolean success;
        private final String feedback;

        public Response(boolean success, String feedback) {
            this.success = success;
            this.feedback = feedback;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getFeedback() {
            return feedback;
        }
    }

    static class Answer {

        private int[] answer;

        public Answer() {
        }

        public int[] getAnswer() {
            return answer;
        }

        public void setAnswer(int[] answer) {
            this.answer = answer;
        }

        public boolean isEmpty() {
            return answer.length == 0;
        }
    }
}