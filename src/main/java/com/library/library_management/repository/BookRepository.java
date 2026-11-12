package com.library.library_management.repository;

import com.library.library_management.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByIsbn(String isbn);

    boolean existsByIsbnAndIdNot(String isbn, Long id);

    boolean existsByAuthor_Id(Long authorId);

    boolean existsByCategory_Id(Long categoryId);

    @Query("""
            select book from Book book
            where (:title is null or lower(book.title) like lower(concat('%', :title, '%')))
              and (:isbn is null or lower(book.isbn) like lower(concat('%', :isbn, '%')))
              and (:authorId is null or book.author.id = :authorId)
              and (:categoryId is null or book.category.id = :categoryId)
            """)
    Page<Book> search(@Param("title") String title,
                      @Param("isbn") String isbn,
                      @Param("authorId") Long authorId,
                      @Param("categoryId") Long categoryId,
                      Pageable pageable);
}
