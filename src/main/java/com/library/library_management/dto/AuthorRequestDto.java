package com.library.library_management.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthorRequestDto {
    @NotBlank(message = "Author name is required")
    private String name;
    private String biography;

    public String getName() {
        return name;
    }

    public String getBiography() {
        return biography;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}
