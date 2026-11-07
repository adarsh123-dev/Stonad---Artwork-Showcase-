package com.Stonad_backend.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "artists")
@JsonPropertyOrder({"id", "name", "email", "bio"})
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String bio;
}