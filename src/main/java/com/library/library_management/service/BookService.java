package com.library.library_management.service;

import com.library.library_management.dto.BookCopyResponseDto;
import com.library.library_management.dto.BookCreateRequestDto;
import com.library.library_management.dto.BookResponseDto;
import com.library.library_management.dto.BookUpdateRequestDto;
import com.library.library_management.entity.Author;
import com.library.library_management.entity.Book;
import com.library.library_management.entity.BookCopy;
import com.library.library_management.entity.BookCopyStatus;
import com.library.library_management.entity.Category;
import com.library.library_management.exception.AuthorNotFoundException;
import com.library.library_management.exception.BookHasLoanHistoryException;
import com.library.library_management.exception.BookNotFoundException;
import com.library.library_management.exception.CategoryNotFoundException;
import com.library.library_management.exception.DuplicateIsbnException;
import com.library.library_management.repository.AuthorRepository;
import com.library.library_management.repository.BookCopyRepository;
import com.library.library_management.repository.BookRepository;
import com.library.library_management.repository.CategoryRepository;
import com.library.library_management.repository.IssueRecordRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final BookCopyRepository bookCopyRepository;
    private final IssueRecordRepository issueRecordRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository,
                       CategoryRepository categoryRepository, BookCopyRepository bookCopyRepository,
                       IssueRecordRepository issueRecordRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.issueRecordRepository = issueRecordRepository;
    }

    @Transactional
    public BookResponseDto addBook(BookCreateRequestDto request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new DuplicateIsbnException("A book with this ISBN already exists");
        }
        Author author = findAuthor(request.getAuthorId());
        Category category = findCategory(request.getCategoryId());

        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setIsbn(request.getIsbn());
        book.setAuthor(author);
        book.setCategory(category);
        for (int i = 0; i < request.getCopies(); i++) {
            book.getCopies().add(new BookCopy(UUID.randomUUID().toString(), book));
        }

        return toDto(bookRepository.save(book));
    }

    @Transactional
    public BookResponseDto updateBook(Long id, BookUpdateRequestDto request) {
        Book book = findBook(id);
        if (bookRepository.existsByIsbnAndIdNot(request.getIsbn(), id)) {
            throw new DuplicateIsbnException("A book with this ISBN already exists");
        }

        book.setTitle(request.getTitle());
        book.setIsbn(request.getIsbn());
        book.setAuthor(findAuthor(request.getAuthorId()));
        book.setCategory(findCategory(request.getCategoryId()));
        return toDto(bookRepository.save(book));
    }

    @Transactional(readOnly = true)
    public Page<BookResponseDto> searchBooks(String title, String isbn, Long authorId,
                                             Long categoryId, Pageable pageable) {
        return bookRepository.search(blankToNull(title), blankToNull(isbn), authorId, categoryId, pageable)
                .map(this::toDto);
    }

    @Transactional(readOnly = true)
    public BookResponseDto getBookById(Long id) {
        return toDto(findBook(id));
    }

    @Transactional(readOnly = true)
    public List<BookCopyResponseDto> getBookCopies(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException("Book not found");
        }
        return bookCopyRepository.findByBook_IdOrderById(bookId).stream()
                .map(copy -> new BookCopyResponseDto(copy.getId(), copy.getBarcode(), copy.getStatus()))
                .toList();
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = findBook(id);
        if (issueRecordRepository.existsByBookCopy_Book_Id(id)) {
            throw new BookHasLoanHistoryException("A book with borrowing history cannot be deleted");
        }
        bookRepository.delete(book);
    }

    private Book findBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));
    }

    private Author findAuthor(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("Author not found"));
    }

    private Category findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private BookResponseDto toDto(Book book) {
        return new BookResponseDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor().getId(),
                book.getAuthor().getName(),
                book.getCategory().getId(),
                book.getCategory().getName(),
                book.getIsbn(),
                Math.toIntExact(bookCopyRepository.countByBook_Id(book.getId())),
                Math.toIntExact(bookCopyRepository.countByBook_IdAndStatus(
                        book.getId(), BookCopyStatus.AVAILABLE))
        );
    }
}
