package com.library.library_management.controller;


import com.library.library_management.dto.BookResponseDto;
import com.library.library_management.dto.BookCreateRequestDto;
import com.library.library_management.dto.BookCopyResponseDto;
import com.library.library_management.dto.BookUpdateRequestDto;
import com.library.library_management.service.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponseDto addBook(@Valid @RequestBody BookCreateRequestDto request) {
        return bookService.addBook(request);
    }

    @GetMapping
    public Page<BookResponseDto> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) Long categoryId,
            @PageableDefault(size = 20, sort = "title") Pageable pageable) {
        return bookService.searchBooks(title, isbn, authorId, categoryId, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getBookById(@PathVariable Long id){
        return ResponseEntity.ok(bookService.getBookById(id));

    }

    @GetMapping("/{id}/copies")
    public List<BookCopyResponseDto> getBookCopies(@PathVariable Long id) {
        return bookService.getBookCopies(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDto> updateBook(
            @PathVariable Long id, @Valid @RequestBody BookUpdateRequestDto request) {
        return ResponseEntity.ok(bookService.updateBook(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id)
    {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

}
