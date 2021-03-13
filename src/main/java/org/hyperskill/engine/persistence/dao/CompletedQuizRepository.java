package org.hyperskill.engine.persistence.dao;

import org.hyperskill.engine.persistence.model.CompletedQuiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompletedQuizRepository extends JpaRepository<CompletedQuiz, Long> {
    Page<CompletedQuiz> findAllByUserId(Long id, Pageable pageable);
}
