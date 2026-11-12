package com.library.library_management.controller;


import com.library.library_management.dto.IssueResponseDto;
import com.library.library_management.dto.IssueCreateRequestDto;
import com.library.library_management.service.IssueRecordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/issues")
public class IssueRecordController {

    private final IssueRecordService issueRecordService;

    public IssueRecordController(IssueRecordService issueRecordService) {
        this.issueRecordService = issueRecordService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IssueResponseDto issueBook(@Valid @RequestBody IssueCreateRequestDto request) {
        return issueRecordService.issueBook(request);
    }

    @GetMapping
    public List<IssueResponseDto> getAllIssueRecords() {
        return issueRecordService.getAllIssueRecords();
    }

    @GetMapping("/{id}")
    public ResponseEntity<IssueResponseDto> getIssueRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(issueRecordService.getIssueRecordById(id));
    }

    @PostMapping("/{id}/return")
    public IssueResponseDto returnBook(@PathVariable Long id) {
        return issueRecordService.returnBook(id);
    }

}