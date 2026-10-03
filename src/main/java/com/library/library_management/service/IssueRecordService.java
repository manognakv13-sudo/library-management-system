package com.library.library_management.service;

import com.library.library_management.dto.IssueResponseDto;
import com.library.library_management.entity.Book;
import com.library.library_management.entity.IssueRecord;
import com.library.library_management.entity.Member;
import com.library.library_management.exception.BookAlreadyIssuedException;
import com.library.library_management.exception.BookNotFoundException;
import com.library.library_management.exception.IssueRecordNotFoundException;
import com.library.library_management.exception.MemberNotFoundException;
import com.library.library_management.repository.BookRepository;
import com.library.library_management.repository.IssueRecordRepository;
import com.library.library_management.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class IssueRecordService {

    private final IssueRecordRepository issueRecordRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public IssueRecordService(
            IssueRecordRepository issueRecordRepository,
            BookRepository bookRepository,
            MemberRepository memberRepository) {

        this.issueRecordRepository = issueRecordRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    public IssueResponseDto issueBook(IssueRecord issueRecord) {

        Long bookId = issueRecord.getBook().getId();
        Long memberId = issueRecord.getMember().getId();

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

         Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));

        if (!book.isAvailable()) {
            throw new BookAlreadyIssuedException("Book is already issued");
        }

        book.setAvailable(false);
        bookRepository.save(book);

        issueRecord.setBook(book);
        issueRecord.setMember(member);

        IssueRecord savedIssue = issueRecordRepository.save(issueRecord);
        return convertToDTO(savedIssue);
    }

    public IssueResponseDto returnBook(Long issueId) {

        IssueRecord issueRecord = issueRecordRepository.findById(issueId)
                .orElseThrow(() -> new IssueRecordNotFoundException("Issue record not found"));

        Book book = issueRecord.getBook();

        book.setAvailable(true);
        bookRepository.save(book);

        issueRecord.setReturnDate(LocalDate.now());

        IssueRecord savedIssue = issueRecordRepository.save(issueRecord);
        return convertToDTO(savedIssue);
    }

    public List<IssueResponseDto> getAllIssueRecords() {

        return issueRecordRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public IssueResponseDto getIssueRecordById(Long id) {
        IssueRecord issueRecord = issueRecordRepository.findById(id)
                .orElseThrow(() -> new IssueRecordNotFoundException("Issued Record Not Found"));
        return convertToDTO(issueRecord);
    }

    public void deleteIssueRecord(Long id) {
        issueRecordRepository.deleteById(id);
    }


    private IssueResponseDto convertToDTO(IssueRecord issueRecord){
        return new IssueResponseDto(
                issueRecord.getId(),
                issueRecord.getBook().getId(),
                issueRecord.getBook().getTitle(),
                issueRecord.getMember().getId(),
                issueRecord.getMember().getName(),
                issueRecord.getIssueDate(),
                issueRecord.getReturnDate()
        );
    }
}