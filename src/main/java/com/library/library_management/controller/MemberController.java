package com.library.library_management.controller;

import com.library.library_management.dto.MemberResponseDto;
import com.library.library_management.entity.Member;
import com.library.library_management.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public MemberResponseDto addMember(@Valid @RequestBody Member member){

        return memberService.addMember(member);
    }

    @GetMapping
    public List<MemberResponseDto> getAllMember(){

        return memberService.getAllMembers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> getByMemberId(@PathVariable Long id){
        return ResponseEntity.ok(memberService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Member> deleteMember(@PathVariable Long id){
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

}
