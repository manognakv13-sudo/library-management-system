package com.library.library_management.repository;

import com.library.library_management.entity.BookCopy;
import com.library.library_management.entity.BookCopyStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {
    long countByBook_Id(Long bookId);

    long countByBook_IdAndStatus(Long bookId, BookCopyStatus status);

    List<BookCopy> findByBook_IdOrderById(Long bookId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select copy from BookCopy copy where copy.id = :id")
    Optional<BookCopy> findByIdForUpdate(@Param("id") Long id);
}
