package com.Stonad_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.Stonad_backend.entity.Artist;
import com.Stonad_backend.repository.ArtistRepository;

@Service
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;
    
    @Autowired
    private ImageStorageService imageStorageService;

 // Register Artist
    public Artist registerArtist(Artist artist) {

        if (artistRepository.existsByEmail(artist.getEmail())) {
            throw new IllegalArgumentException("Email already registered!");
        }

        return artistRepository.save(artist);
    }
    
 // Get all artists
    public List<Artist> getAllArtists() {
        return artistRepository.findAll();
    }

    // Get artist by ID
    public Optional<Artist> getArtistById(Long id) {
        return artistRepository.findById(id);
    }
    
    
 // Update Artist
    public Optional<Artist> updateArtist(Long id, Artist updatedArtist) {

        Optional<Artist> existingArtist = artistRepository.findById(id);

        if (existingArtist.isPresent()) {

            Artist artist = existingArtist.get();

            artist.setName(updatedArtist.getName());
            artist.setEmail(updatedArtist.getEmail());
            artist.setBio(updatedArtist.getBio());

            return Optional.of(artistRepository.save(artist));
        }

        return Optional.empty();
    }
    
 // Delete Artist
    public boolean deleteArtist(Long id) {

        if (artistRepository.existsById(id)) {

            artistRepository.deleteById(id);
            return true;
        }

        return false;
    }
    
    
    
 // Register Artist with Profile Image
    public Artist registerArtistWithImage(
            Artist artist,
            MultipartFile image) throws IOException {

        if (artistRepository.existsByEmail(artist.getEmail())) {
            throw new IllegalArgumentException("Email already registered!");
        }

        String filename = imageStorageService.storeImage(image);

        artist.setProfileImage("uploads/" + filename);

        return artistRepository.save(artist);
    }
}