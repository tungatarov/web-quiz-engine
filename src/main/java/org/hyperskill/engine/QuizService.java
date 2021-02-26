package org.hyperskill.engine;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class QuizService {

    private final Set<Quiz> quizSet;

    public QuizService() {
        this.quizSet = new HashSet<>();
    }

    public Set<Quiz> getQuizSet() {
        return quizSet;
    }

    public void addQuiz(Quiz quiz) {
        quizSet.add(quiz);
    }

    public Quiz getQuizById(int id) {
        return quizSet.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
