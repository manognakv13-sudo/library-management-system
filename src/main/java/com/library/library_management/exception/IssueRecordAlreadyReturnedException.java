package com.library.library_management.exception;

public class IssueRecordAlreadyReturnedException extends RuntimeException {
    public IssueRecordAlreadyReturnedException(String message) {
        super(message);
    }
}
