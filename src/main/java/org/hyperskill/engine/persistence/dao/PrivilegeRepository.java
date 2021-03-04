package org.hyperskill.engine.persistence.dao;

import org.hyperskill.engine.persistence.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    Privilege findByName(String name);
}