package com.library.library_management.repository;

import com.library.library_management.entity.IssueRecord;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IssueRecordRepository extends JpaRepository<IssueRecord, Long> {
    boolean existsByBookCopy_Book_Id(Long bookId);

    boolean existsByMember_Id(Long memberId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select issue from IssueRecord issue where issue.id = :id")
    Optional<IssueRecord> findByIdForUpdate(@Param("id") Long id);

    List<IssueRecord> findByReturnDateIsNullAndDueDateBefore(LocalDate date);
}
