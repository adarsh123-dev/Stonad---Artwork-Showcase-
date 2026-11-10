
package com.Stonad_backend.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.Stonad_backend.entity.Visitor;
import com.Stonad_backend.exception.DuplicateEmailException;
import com.Stonad_backend.repository.VisitorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VisitorService {

    private final VisitorRepository visitorRepository;

    public Visitor registerVisitor(Visitor visitor) {

    	if (visitorRepository.existsByEmail(visitor.getEmail())) {
    	    throw new DuplicateEmailException("Email already registered!");
    	}

        visitor.setCreatedAt(LocalDateTime.now());

        return visitorRepository.save(visitor);
    }
    

public Visitor loginVisitor(String email, String password) {

    Visitor visitor = visitorRepository.findByEmail(email)
            .orElseThrow(() ->
                    new RuntimeException("Visitor not found!"));

    if (!visitor.getPassword().equals(password)) {
        throw new RuntimeException("Invalid password!");
    }

    return visitor;
}

}
