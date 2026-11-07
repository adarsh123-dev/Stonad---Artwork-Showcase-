
package com.Stonad_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Stonad_backend.entity.Artwork;

@Repository
public interface ArtworkRepository extends JpaRepository<Artwork, Long> {

}
