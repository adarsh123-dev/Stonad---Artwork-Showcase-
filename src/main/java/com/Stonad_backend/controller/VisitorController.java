
package com.Stonad_backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Stonad_backend.entity.Visitor;
import com.Stonad_backend.service.VisitorService;

import jakarta.validation.Valid;
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
        @Valid @RequestBody Visitor visitor) {

    Visitor loggedInVisitor = visitorService.loginVisitor(
            visitor.getEmail(),
            visitor.getPassword()
    );

    return ResponseEntity.ok(loggedInVisitor);
}




@GetMapping("/{id}")
public ResponseEntity<Visitor> getVisitorById(@PathVariable Long id) {

    Visitor visitor = visitorService.getVisitorById(id);

    return ResponseEntity.ok(visitor);
}




@GetMapping
public ResponseEntity<List<Visitor>> getAllVisitors() {

    List<Visitor> visitors = visitorService.getAllVisitors();

    return ResponseEntity.ok(visitors);
}



@PutMapping("/{id}")
public ResponseEntity<Visitor> updateVisitor(
        @PathVariable Long id,
        @Valid  @RequestBody Visitor visitor) {

    Visitor updatedVisitor = visitorService.updateVisitor(id, visitor);

    return ResponseEntity.ok(updatedVisitor);
}


@DeleteMapping("/{id}")
public ResponseEntity<String> deleteVisitor(@PathVariable Long id) {

    visitorService.deleteVisitor(id);

    return ResponseEntity.ok("Visitor deleted successfully!");
}

}
