package com.library.library_management.service;

import com.library.library_management.dto.BookResponseDto;
import com.library.library_management.entity.Book;
import com.library.library_management.exception.BookNotFoundException;
import com.library.library_management.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookResponseDto addBook(Book book){

        Book savedBook = bookRepository.save(book);
        return converToDto(savedBook);
    }

    public List<BookResponseDto> getAllBooks(){

        return bookRepository.findAll()
                .stream()
                .map(this::converToDto)
                .toList();
    }

    public BookResponseDto getBookById(Long id){

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book Not Found"));
        return converToDto(book);
    }

    public void deleteBook(Long id){
        bookRepository.deleteById(id);
    }

    private BookResponseDto converToDto(Book book){
        return new BookResponseDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.isAvailable()
        );
    }


}
