package com.Stonad_backend.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Stonad_backend.entity.Artist;
import com.Stonad_backend.service.ArtistService;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

 // Register Artist
    @PostMapping
    public ResponseEntity<Artist> registerArtist(@RequestBody Artist artist) {

        Artist savedArtist = artistService.registerArtist(artist);

        return ResponseEntity.status(201).body(savedArtist);
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
    
    
    
 // Register Artist with Profile Image
    @PostMapping("/register")
    public ResponseEntity<Artist> registerArtistWithImage(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("bio") String bio,
            @RequestParam("image") MultipartFile image
    ) throws IOException {

        Artist artist = new Artist();

        artist.setName(name);
        artist.setEmail(email);
        artist.setPassword(password);
        artist.setBio(bio);

        Artist savedArtist =
                artistService.registerArtistWithImage(artist, image);

        return ResponseEntity.status(201).body(savedArtist);
    }
}