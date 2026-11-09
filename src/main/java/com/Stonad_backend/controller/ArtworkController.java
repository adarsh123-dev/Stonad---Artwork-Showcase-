
package com.Stonad_backend.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.Stonad_backend.entity.Artist;
import com.Stonad_backend.entity.Artwork;
import com.Stonad_backend.entity.Category;
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
    

@PostMapping(value = "/upload",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public Artwork uploadArtwork(

        @RequestParam String title,

        @RequestParam String description,

        @RequestParam Long artistId,

        @RequestParam Long categoryId,

        @RequestParam("image") MultipartFile image
) throws IOException {

    Artwork artwork = new Artwork();

    artwork.setTitle(title);
    artwork.setDescription(description);

    Artist artist = new Artist();
    artist.setId(artistId);

    Category category = new Category();
    category.setId(categoryId);

    artwork.setArtist(artist);
    artwork.setCategory(category);

    return artworkService.saveArtworkWithImage(artwork, image);
}



@GetMapping("/search")
public ResponseEntity<List<Artwork>> searchArtwork(
        @RequestParam String title) {

    List<Artwork> artworks =
            artworkService.searchArtworkByTitle(title);

    return ResponseEntity.ok(artworks);
}


//Filter Artwork By Category ID
@GetMapping("/category/{categoryId}")
public ResponseEntity<List<Artwork>> getArtworkByCategory(
     @PathVariable Long categoryId) {

 List<Artwork> artworks =
         artworkService.getArtworkByCategory(categoryId);

 return ResponseEntity.ok(artworks);
}

}
