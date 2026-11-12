package com.library.library_management.service;

import com.library.library_management.dto.AuthorRequestDto;
import com.library.library_management.dto.AuthorResponseDto;
import com.library.library_management.entity.Author;
import com.library.library_management.exception.AuthorHasBooksException;
import com.library.library_management.exception.AuthorNotFoundException;
import com.library.library_management.repository.AuthorRepository;
import com.library.library_management.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public AuthorResponseDto create(AuthorRequestDto request) {
        return toDto(authorRepository.save(new Author(request.getName(), request.getBiography())));
    }

    @Transactional(readOnly = true)
    public List<AuthorResponseDto> findAll() {
        return authorRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public AuthorResponseDto findById(Long id) {
        return toDto(findAuthor(id));
    }

    @Transactional
    public AuthorResponseDto update(Long id, AuthorRequestDto request) {
        Author author = findAuthor(id);
        author.setName(request.getName());
        author.setBiography(request.getBiography());
        return toDto(authorRepository.save(author));
    }

    @Transactional
    public void delete(Long id) {
        Author author = findAuthor(id);
        if (bookRepository.existsByAuthor_Id(id)) {
            throw new AuthorHasBooksException("An author linked to books cannot be deleted");
        }
        authorRepository.delete(author);
    }

    private Author findAuthor(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("Author not found"));
    }

    private AuthorResponseDto toDto(Author author) {
        return new AuthorResponseDto(author.getId(), author.getName(), author.getBiography());
    }
}
