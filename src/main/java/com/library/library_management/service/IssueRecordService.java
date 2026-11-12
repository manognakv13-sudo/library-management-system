package com.library.library_management.service;

import com.library.library_management.dto.IssueCreateRequestDto;
import com.library.library_management.dto.IssueResponseDto;
import com.library.library_management.entity.BookCopy;
import com.library.library_management.entity.BookCopyStatus;
import com.library.library_management.entity.IssueRecord;
import com.library.library_management.entity.Member;
import com.library.library_management.entity.MemberStatus;
import com.library.library_management.exception.BookAlreadyIssuedException;
import com.library.library_management.exception.BookNotFoundException;
import com.library.library_management.exception.IssueRecordAlreadyReturnedException;
import com.library.library_management.exception.IssueRecordNotFoundException;
import com.library.library_management.exception.InactiveMemberException;
import com.library.library_management.exception.InvalidLoanStateException;
import com.library.library_management.exception.MemberNotFoundException;
import com.library.library_management.repository.BookCopyRepository;
import com.library.library_management.repository.IssueRecordRepository;
import com.library.library_management.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class IssueRecordService {

    private final IssueRecordRepository issueRecordRepository;
    private final BookCopyRepository bookCopyRepository;
    private final MemberRepository memberRepository;

    public IssueRecordService(IssueRecordRepository issueRecordRepository,
                              BookCopyRepository bookCopyRepository,
                              MemberRepository memberRepository) {
        this.issueRecordRepository = issueRecordRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public IssueResponseDto issueBook(IssueCreateRequestDto request) {
        BookCopy copy = bookCopyRepository.findByIdForUpdate(request.getBookCopyId())
                .orElseThrow(() -> new BookNotFoundException("Book copy not found"));
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));
        if (member.getStatus() != MemberStatus.ACTIVE) {
            throw new InactiveMemberException("Inactive members cannot borrow books");
        }
        if (copy.getStatus() != BookCopyStatus.AVAILABLE) {
            throw new BookAlreadyIssuedException("Book copy is not available");
        }

        copy.setStatus(BookCopyStatus.ISSUED);
        IssueRecord issue = new IssueRecord();
        issue.setBookCopy(copy);
        issue.setMember(member);
        issue.setIssueDate(LocalDate.now());
        return toDto(issueRecordRepository.save(issue));
    }

    @Transactional
    public IssueResponseDto returnBook(Long issueId) {
        IssueRecord issue = issueRecordRepository.findByIdForUpdate(issueId)
                .orElseThrow(() -> new IssueRecordNotFoundException("Issue record not found"));
        if (issue.getReturnDate() != null) {
            throw new IssueRecordAlreadyReturnedException("This loan has already been returned");
        }
        BookCopy copy = issue.getBookCopy();
        if (copy.getStatus() != BookCopyStatus.ISSUED) {
            throw new InvalidLoanStateException("The copy is not marked as issued for this loan");
        }
        copy.setStatus(BookCopyStatus.AVAILABLE);
        issue.setReturnDate(LocalDate.now());
        return toDto(issue);
    }

    @Transactional(readOnly = true)
    public List<IssueResponseDto> getAllIssueRecords() {
        return issueRecordRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public IssueResponseDto getIssueRecordById(Long id) {
        IssueRecord issue = issueRecordRepository.findById(id)
                .orElseThrow(() -> new IssueRecordNotFoundException("Issue record not found"));
        return toDto(issue);
    }

    private IssueResponseDto toDto(IssueRecord issue) {
        BookCopy copy = issue.getBookCopy();
        return new IssueResponseDto(
                issue.getId(),
                copy.getBook().getId(),
                copy.getId(),
                copy.getBook().getTitle(),
                issue.getMember().getId(),
                issue.getMember().getName(),
                issue.getIssueDate(),
                issue.getReturnDate()
        );
    }
}
