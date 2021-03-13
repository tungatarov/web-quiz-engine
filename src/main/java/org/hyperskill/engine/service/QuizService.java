package org.hyperskill.engine.service;

import org.hyperskill.engine.persistence.dao.CompletedQuizRepository;
import org.hyperskill.engine.persistence.dao.QuizRepository;
import org.hyperskill.engine.persistence.dao.UserRepository;
import org.hyperskill.engine.persistence.model.CompletedQuiz;
import org.hyperskill.engine.persistence.model.Quiz;
import org.hyperskill.engine.persistence.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class QuizService {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final CompletedQuizRepository completedQuizRepository;
    private final HttpServletRequest request;


    @Autowired
    public QuizService(QuizRepository quizRepository, UserRepository userRepository, CompletedQuizRepository completedQuizRepository, HttpServletRequest request) {
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
        this.completedQuizRepository = completedQuizRepository;
        this.request = request;
    }


    public Quiz addQuiz(Quiz quiz) {
        User user = userRepository.findByEmail(request.getUserPrincipal().getName());
        quiz.setAuthor(user);
        return quizRepository.save(quiz);
    }

    public Page<Quiz> getAllQuizzes(Integer page) {
        Pageable paging = PageRequest.of(page, 10, Sort.by("id"));
        return quizRepository.findAll(paging);
    }

    public Quiz getQuizById(Long id) {
        return quizRepository.findById(id).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<String> deleteUserCreatedQuizById(Long id) {
        User user = userRepository.findByEmail(request.getUserPrincipal().getName());
        Quiz quiz = getQuizById(id);
        if (!quiz.getAuthor().getId().equals(user.getId())) {
            throw  new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        quizRepository.delete(quiz);
        return ResponseEntity.noContent().build();
    }

    public Response solveQuiz(Long id, Answer answer) {
        User user = userRepository.findByEmail(request.getUserPrincipal().getName());
        Quiz quiz = getQuizById(id);
        if (answer.isEmpty() && quiz.isAnswerNull() || Arrays.equals(answer.getAnswer(), quiz.getAnswer())) {
            CompletedQuiz completedQuiz = new CompletedQuiz();
            completedQuiz.setQuizId(quiz.getId());
            completedQuiz.setCompletedAt(LocalDateTime.now());
            completedQuiz.setUser(user);
            completedQuizRepository.save(completedQuiz);
            return Response.CORRECT_ANSWER;
        } else {
            return Response.WRONG_ANSWER;
        }
    }

    public Page<CompletedQuiz> getAllCompletedQuizzes(Integer page) {
        User user = userRepository.findByEmail(request.getUserPrincipal().getName());
        Pageable pageable = PageRequest.of(page, 10, Sort.by("completedAt").descending());
        return completedQuizRepository.findAllByUserId(user.getId(), pageable);
    }

    public static class Response {

        public final static QuizService.Response CORRECT_ANSWER = new QuizService.Response(true, "Congratulations, you're right!");
        public final static QuizService.Response WRONG_ANSWER = new QuizService.Response(false, "Wrong answer! Please, try again.");

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

    public static class Answer {

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
