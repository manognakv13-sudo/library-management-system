package com.library.library_management.controller;

import com.library.library_management.dto.MemberResponseDto;
import com.library.library_management.dto.MemberRequestDto;
import com.library.library_management.dto.MemberStatusRequestDto;
import com.library.library_management.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponseDto addMember(@Valid @RequestBody MemberRequestDto request) {
        return memberService.addMember(request);
    }

    @GetMapping
    public List<MemberResponseDto> getAllMember(){

        return memberService.getAllMembers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> getByMemberId(@PathVariable Long id){
        return ResponseEntity.ok(memberService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberResponseDto> updateMember(
            @PathVariable Long id, @Valid @RequestBody MemberRequestDto request) {
        return ResponseEntity.ok(memberService.updateMember(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<MemberResponseDto> updateStatus(
            @PathVariable Long id, @Valid @RequestBody MemberStatusRequestDto request) {
        return ResponseEntity.ok(memberService.updateStatus(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id){
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

}
