
package com.Stonad_backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.Stonad_backend.entity.Visitor;
import com.Stonad_backend.exception.DuplicateEmailException;
import com.Stonad_backend.exception.InvalidPasswordException;
import com.Stonad_backend.exception.VisitorNotFoundException;
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
		    .orElseThrow(() -> new VisitorNotFoundException("Visitor not found!"));

	if (!visitor.getPassword().equals(password)) {
	    throw new InvalidPasswordException("Invalid password!");
	}

    return visitor;
}



public Visitor getVisitorById(Long id) {

    return visitorRepository.findById(id)
            .orElseThrow(() -> new VisitorNotFoundException(
                    "Visitor not found!"
            ));
}


public List<Visitor> getAllVisitors() {
    return visitorRepository.findAll();
}


public Visitor updateVisitor(Long id, Visitor updatedVisitor) {

    Visitor existingVisitor = visitorRepository.findById(id)
            .orElseThrow(() -> new VisitorNotFoundException(
                    "Visitor not found!"
            ));
    
    if (!existingVisitor.getEmail().equals(updatedVisitor.getEmail())
            && visitorRepository.existsByEmail(updatedVisitor.getEmail())) {

        throw new DuplicateEmailException("Email already registered!");
    }

    existingVisitor.setName(updatedVisitor.getName());
    existingVisitor.setEmail(updatedVisitor.getEmail());

    return visitorRepository.save(existingVisitor);
}


public void deleteVisitor(Long id) {

    Visitor visitor = visitorRepository.findById(id)
            .orElseThrow(() -> new VisitorNotFoundException(
                    "Visitor not found!"
            ));

    visitorRepository.delete(visitor);
}

}
