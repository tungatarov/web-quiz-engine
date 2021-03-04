package org.hyperskill.engine.persistence.dao;

import org.hyperskill.engine.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}