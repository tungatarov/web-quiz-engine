package org.hyperskill.engine.persistence.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "quizzes")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Pattern(regexp = "(.*[a-z].*)")
    private String title;

    @NotNull
    @Pattern(regexp = "(.*[a-z].*)")
    private String text;

    @Size(min = 2)
    private String[] options;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int[] answer;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

    public Quiz() {}

    public Quiz(String title, String text, String[] options, int[] answer) {
        this.title = title;
        this.text = text;
        this.options = options;
        this.answer = answer;
    }


    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String[] getOptions() {
        return options;
    }

    public int[] getAnswer() {
        return answer;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public void setAnswer(int[] answer) {
        this.answer = answer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JsonIgnore
    public boolean isAnswerNull() {
        return answer == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quiz quiz = (Quiz) o;
        return  title.equals(quiz.title) &&
                text.equals(quiz.text) &&
                Arrays.equals(options, quiz.options) &&
                Arrays.equals(answer, quiz.answer);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(title, text);
        result = 31 * result + Arrays.hashCode(options);
        result = 31 * result + Arrays.hashCode(answer);
        return result;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", options=" + Arrays.toString(options) +
                ", answer=" + Arrays.toString(answer) +
                '}';
    }
}