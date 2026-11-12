package com.library.library_management.dto;

import java.time.LocalDate;

public class IssueResponseDto {
    private Long issueId;
    private Long bookId;
    private Long bookCopyId;
    private String bookTitle;

    private Long memberId;
    private String memberName;

    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private boolean overdue;

    public IssueResponseDto(Long issueId, Long bookId, Long bookCopyId, String bookTitle,
                            Long memberId, String memberName, LocalDate issueDate,
                            LocalDate dueDate, LocalDate returnDate, boolean overdue) {
        this.issueId = issueId;
        this.bookId = bookId;
        this.bookCopyId = bookCopyId;
        this.bookTitle = bookTitle;
        this.memberId = memberId;
        this.memberName = memberName;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.overdue = overdue;
    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public Long getBookId() {
        return bookId;
    }

    public Long getBookCopyId() {
        return bookCopyId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean isOverdue() {
        return overdue;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}
