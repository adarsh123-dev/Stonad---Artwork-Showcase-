
package com.Stonad_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Stonad_backend.entity.Visitor;
import com.Stonad_backend.service.VisitorService;


import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/visitors")
@RequiredArgsConstructor
public class VisitorController {

    private final VisitorService visitorService;

    @PostMapping("/register")
    public ResponseEntity<Visitor> registerVisitor(
            @RequestBody Visitor visitor) {

        Visitor savedVisitor =
                visitorService.registerVisitor(visitor);

        return new ResponseEntity<>(
                savedVisitor, HttpStatus.CREATED);
    }
    

@PostMapping("/login")
public ResponseEntity<Visitor> loginVisitor(
        @RequestBody Visitor visitor) {

    Visitor loggedInVisitor = visitorService.loginVisitor(
            visitor.getEmail(),
            visitor.getPassword()
    );

    return ResponseEntity.ok(loggedInVisitor);
}

}
