
package com.Stonad_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "artworks")
@Data
public class Artwork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String imageUrl;
    
    

    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}


//@ManyToOne
//
//Many artworks can belong to one artist or one category.
//
//@JoinColumn
//
//Creates the foreign key column in the artwork table.
//
//For example:
//- artist_id references the Artist table.
//- category_id references the Category table.
