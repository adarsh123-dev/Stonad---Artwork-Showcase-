
package com.Stonad_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Stonad_backend.entity.Artist;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

    boolean existsByEmail(String email);

}
