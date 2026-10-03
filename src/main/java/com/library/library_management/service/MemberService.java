package com.library.library_management.service;

import com.library.library_management.dto.MemberResponseDto;
import com.library.library_management.entity.Member;
import com.library.library_management.exception.BookNotFoundException;
import com.library.library_management.exception.MemberNotFoundException;
import com.library.library_management.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponseDto addMember(Member member){

        Member savedMember = memberRepository.save(member);
        return convertToDto(savedMember);
    }

    public List<MemberResponseDto> getAllMembers(){

        return memberRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    public MemberResponseDto getById(Long id){

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("member Not Found"));
        return convertToDto(member);
    }

    public void deleteMember(Long id){
        memberRepository.deleteById(id);
    }

    private MemberResponseDto convertToDto(Member member){
        return new MemberResponseDto(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getPhone()
        );
    }
}
