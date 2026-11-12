package com.library.library_management.dto;

public class AuthorResponseDto {
    private final Long id;
    private final String name;
    private final String biography;

    public AuthorResponseDto(Long id, String name, String biography) {
        this.id = id;
        this.name = name;
        this.biography = biography;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBiography() {
        return biography;
    }
}
