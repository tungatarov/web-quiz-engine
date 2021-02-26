package org.hyperskill.engine;

import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class QuizController {

    private final List<Quiz> quizList = new ArrayList<>();

    public QuizController() {}

    @PostMapping("/api/quizzes")
    public Quiz addQuiz(@RequestBody Quiz quiz) {
        quizList.add(quiz);
        return quizList.get(quiz.getId() - 1);
    }

    @PostMapping("/api/quizzes/{id}/solve")
    public Answer passAnswer(@PathVariable int id, @RequestParam("answer") int option) {
        return option == quizList.get(id - 1).getAnswer()
                ? Answer.CORRECT_ANSWER
                : Answer.WRONG_ANSWER;
    }

    @GetMapping("/api/quizzes/{id}")
    public Quiz getQuiz(@PathVariable int id) {
        try {
            return quizList.get(id - 1);

        } catch (IndexOutOfBoundsException e) {
            throw new QuizNotFoundException("not found");
        }
    }

    @GetMapping("/api/quizzes")
    public List<Quiz> getQuizzes() {
        return quizList;
    }


    static class Answer {

        private final static Answer CORRECT_ANSWER = new Answer(true, "Congratulations, you're right!");
        private final static Answer WRONG_ANSWER = new Answer(false, "Wrong answer! Please, try again.");

        private final boolean success;
        private final String feedback;

        public Answer(boolean success, String feedback) {
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
}
