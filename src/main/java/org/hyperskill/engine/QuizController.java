package org.hyperskill.engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Set;

@RestController
@RequestMapping("/api/quizzes")
@Component
public class QuizController {

    private static final String NOT_FOUND_MESSAGE = "not found";
    private final QuizService quizService;

    @Autowired
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping()
    public Quiz addQuiz(@Valid @RequestBody Quiz quiz) {
        quizService.addQuiz(quiz);
        return quiz;
    }

    @PostMapping(value = "/{id}/solve", consumes = "application/json")
    public Response sendAnswer(@PathVariable int id, @RequestBody Answer answer) {
        return answer.isEmpty() && getQuiz(id).isAnswerNull() || Arrays.equals(answer.getAnswer(), getQuiz(id).getAnswer())
                ? Response.CORRECT_ANSWER
                : Response.WRONG_ANSWER;
    }

    @GetMapping("/{id}")
    public Quiz getQuiz(@PathVariable int id) {
        Quiz quiz = quizService.getQuizById(id);
        if (quiz != null) {
            return quiz;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE);
        }
    }

    @GetMapping()
    public Set<Quiz> getQuizzes() {
        return quizService.getQuizSet();
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
