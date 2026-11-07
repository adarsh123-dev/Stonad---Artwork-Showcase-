
package com.Stonad_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.Stonad_backend.entity.Artwork;
import com.Stonad_backend.repository.ArtworkRepository;

@Service
public class ArtworkService {

    private final ArtworkRepository artworkRepository;

    // Constructor Injection
    public ArtworkService(ArtworkRepository artworkRepository) {
        this.artworkRepository = artworkRepository;
    }

    // Save Artwork
    public Artwork saveArtwork(Artwork artwork) {
        return artworkRepository.save(artwork);
    }

    // Get All Artworks
    public List<Artwork> getAllArtworks() {
        return artworkRepository.findAll();
    }

    // Get Artwork By ID using Optional
    public Artwork getArtworkById(Long id) {

        Optional<Artwork> optionalArtwork = artworkRepository.findById(id);

        if (optionalArtwork.isPresent()) {
            return optionalArtwork.get();
        }

        throw new RuntimeException("Artwork not found with id: " + id);
    }

    // Delete Artwork
    public void deleteArtwork(Long id) {

        Optional<Artwork> optionalArtwork = artworkRepository.findById(id);

        if (optionalArtwork.isPresent()) {
            artworkRepository.deleteById(id);
        } else {
            throw new RuntimeException("Artwork not found with id: " + id);
        }
    }
    

    // Update Artwork using Optional
   public Artwork updateArtwork(Long id, Artwork updatedArtwork) {

       Optional<Artwork> optionalArtwork =
               artworkRepository.findById(id);

       if (optionalArtwork.isPresent()) {

           Artwork existingArtwork = optionalArtwork.get();

           existingArtwork.setTitle(updatedArtwork.getTitle());
           existingArtwork.setDescription(updatedArtwork.getDescription());
           existingArtwork.setImageUrl(updatedArtwork.getImageUrl());
           existingArtwork.setArtist(updatedArtwork.getArtist());
           existingArtwork.setCategory(updatedArtwork.getCategory());

           return artworkRepository.save(existingArtwork);
       }

       throw new RuntimeException("Artwork not found with id: " + id);
   }

}
