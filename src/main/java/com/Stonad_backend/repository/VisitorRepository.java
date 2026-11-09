
package com.Stonad_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Stonad_backend.entity.Visitor;

public interface VisitorRepository
        extends JpaRepository<Visitor, Long> {

	boolean existsByEmail(String email);
	

    Optional<Visitor> findByEmail(String email);
}
