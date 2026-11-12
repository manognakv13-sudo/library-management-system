package com.library.library_management.exception;

public class BookHasLoanHistoryException extends RuntimeException {
    public BookHasLoanHistoryException(String message) {
        super(message);
    }
}
