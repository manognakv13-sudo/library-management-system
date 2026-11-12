package com.library.library_management.dto;

import com.library.library_management.entity.BookCopyStatus;

public class BookCopyResponseDto {
    private final Long id;
    private final String barcode;
    private final BookCopyStatus status;

    public BookCopyResponseDto(Long id, String barcode, BookCopyStatus status) {
        this.id = id;
        this.barcode = barcode;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getBarcode() {
        return barcode;
    }

    public BookCopyStatus getStatus() {
        return status;
    }
}
