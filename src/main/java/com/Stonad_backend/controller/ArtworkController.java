
package com.Stonad_backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.Stonad_backend.entity.Artwork;
import com.Stonad_backend.service.ArtworkService;

@RestController
@RequestMapping("/api/artworks")
public class ArtworkController {

    private final ArtworkService artworkService;

    // Constructor Injection
    public ArtworkController(ArtworkService artworkService) {
        this.artworkService = artworkService;
    }

    // Create Artwork
    @PostMapping
    public Artwork saveArtwork(@RequestBody Artwork artwork) {
        return artworkService.saveArtwork(artwork);
    }

    // Get All Artworks
    @GetMapping
    public List<Artwork> getAllArtworks() {
        return artworkService.getAllArtworks();
    }

    // Get Artwork By ID
    @GetMapping("/{id}")
    public Artwork getArtworkById(@PathVariable Long id) {
        return artworkService.getArtworkById(id);
    }

    // Update Artwork
    @PutMapping("/{id}")
    public Artwork updateArtwork(
            @PathVariable Long id,
            @RequestBody Artwork updatedArtwork) {

        return artworkService.updateArtwork(id, updatedArtwork);
    }

    // Delete Artwork
    @DeleteMapping("/{id}")
    public String deleteArtwork(@PathVariable Long id) {
        artworkService.deleteArtwork(id);
        return "Artwork deleted successfully";
    }
}
