package com.library.library_management.service;

import com.library.library_management.dto.MemberRequestDto;
import com.library.library_management.dto.MemberResponseDto;
import com.library.library_management.dto.MemberStatusRequestDto;
import com.library.library_management.entity.Member;
import com.library.library_management.entity.MemberStatus;
import com.library.library_management.exception.MemberHasLoanHistoryException;
import com.library.library_management.exception.MemberNotFoundException;
import com.library.library_management.exception.DuplicateEmailException;
import com.library.library_management.repository.IssueRecordRepository;
import com.library.library_management.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final IssueRecordRepository issueRecordRepository;

    public MemberService(MemberRepository memberRepository, IssueRecordRepository issueRecordRepository) {
        this.memberRepository = memberRepository;
        this.issueRecordRepository = issueRecordRepository;
    }

    @Transactional
    public MemberResponseDto addMember(MemberRequestDto request) {
        String email = normalizeEmail(request.getEmail());
        if (memberRepository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateEmailException("A member with this email already exists");
        }

        Member member = new Member();
        member.setName(request.getName().trim());
        member.setEmail(email);
        member.setPhone(request.getPhone());
        member.setStatus(MemberStatus.ACTIVE);
        return toDto(memberRepository.save(member));
    }

    @Transactional(readOnly = true)
    public List<MemberResponseDto> getAllMembers() {
        return memberRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public MemberResponseDto getById(Long id) {
        return toDto(findMember(id));
    }

    @Transactional
    public MemberResponseDto updateMember(Long id, MemberRequestDto request) {
        Member member = findMember(id);
        String email = normalizeEmail(request.getEmail());
        if (memberRepository.existsByEmailIgnoreCaseAndIdNot(email, id)) {
            throw new DuplicateEmailException("A member with this email already exists");
        }

        member.setName(request.getName().trim());
        member.setEmail(email);
        member.setPhone(request.getPhone());
        return toDto(memberRepository.save(member));
    }

    @Transactional
    public MemberResponseDto updateStatus(Long id, MemberStatusRequestDto request) {
        Member member = findMember(id);
        member.setStatus(request.getStatus());
        return toDto(memberRepository.save(member));
    }

    @Transactional
    public void deleteMember(Long id) {
        Member member = findMember(id);
        if (issueRecordRepository.existsByMember_Id(id)) {
            throw new MemberHasLoanHistoryException("A member with borrowing history cannot be deleted");
        }
        memberRepository.delete(member);
    }

    private Member findMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private MemberResponseDto toDto(Member member) {
        return new MemberResponseDto(
                member.getId(), member.getName(), member.getEmail(), member.getPhone(), member.getStatus());
    }
}
