package com.library.library_management.exception;

public class MemberHasLoanHistoryException extends RuntimeException {
    public MemberHasLoanHistoryException(String message) {
        super(message);
    }
}
