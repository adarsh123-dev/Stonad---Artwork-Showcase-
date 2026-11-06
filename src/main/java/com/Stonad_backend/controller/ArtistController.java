package com.Stonad_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Stonad_backend.entity.Artist;
import com.Stonad_backend.service.ArtistService;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    // Register Artist
    @PostMapping
    public Artist registerArtist(@RequestBody Artist artist) {

        return artistService.registerArtist(artist);
    }
    
 // Get all artists
    @GetMapping
    public List<Artist> getAllArtists() {

        return artistService.getAllArtists();
    }

    // Get artist by ID
    @GetMapping("/{id}")
    public ResponseEntity<Artist> getArtistById(@PathVariable Long id) {

        return artistService.getArtistById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    
 // Update Artist
    @PutMapping("/{id}")
    public ResponseEntity<Artist> updateArtist(
            @PathVariable Long id,
            @RequestBody Artist artist) {

        return artistService.updateArtist(id, artist)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    
 // Delete Artist
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteArtist(@PathVariable Long id) {

        boolean deleted = artistService.deleteArtist(id);

        if (deleted) {
            return ResponseEntity.ok("Artist deleted successfully");
        }

        return ResponseEntity.notFound().build();
    }
}