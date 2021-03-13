package org.hyperskill.engine.persistence.dao;

import org.hyperskill.engine.persistence.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Quiz findByTitle(String title);
}
