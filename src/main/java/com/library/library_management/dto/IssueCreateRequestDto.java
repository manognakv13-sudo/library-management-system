package com.library.library_management.dto;

import jakarta.validation.constraints.NotNull;

public class IssueCreateRequestDto {
    @NotNull(message = "Book copy ID is required")
    private Long bookCopyId;

    @NotNull(message = "Member ID is required")
    private Long memberId;

    public Long getBookCopyId() {
        return bookCopyId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setBookCopyId(Long bookCopyId) {
        this.bookCopyId = bookCopyId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
