package com.library.library_management.service;

import com.library.library_management.dto.AuthorRequestDto;
import com.library.library_management.dto.AuthorResponseDto;
import com.library.library_management.entity.Author;
import com.library.library_management.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public AuthorResponseDto create(AuthorRequestDto request) {
        Author saved = authorRepository.save(new Author(request.getName(), request.getBiography()));
        return toDto(saved);
    }

    public List<AuthorResponseDto> findAll() {
        return authorRepository.findAll().stream().map(this::toDto).toList();
    }

    private AuthorResponseDto toDto(Author author) {
        return new AuthorResponseDto(author.getId(), author.getName(), author.getBiography());
    }
}
