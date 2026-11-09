
package com.Stonad_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Stonad_backend.entity.Artwork;

@Repository
public interface ArtworkRepository extends JpaRepository<Artwork, Long> {
	
	List<Artwork> findByTitleContainingIgnoreCase(String title);
	
	// Filter artwork by category ID
    List<Artwork> findByCategoryId(Long categoryId);

}
