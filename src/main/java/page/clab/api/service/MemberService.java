package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import page.clab.api.auth.util.AuthUtil;
import page.clab.api.exception.AssociatedAccountExistsException;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.exception.SearchResultNotExistException;
import page.clab.api.repository.MemberRepository;
import page.clab.api.type.dto.MemberUpdateRequestDto;
import page.clab.api.type.dto.MemberRequestDto;
import page.clab.api.type.dto.MemberResponseDto;
import page.clab.api.type.entity.Member;
import page.clab.api.type.etc.Role;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public void createMember(MemberRequestDto memberRequestDto) throws PermissionDeniedException {
        checkMemberAdminRole();
        if (memberRepository.findById(memberRequestDto.getId()).isPresent())
            throw new AssociatedAccountExistsException();
        if (memberRepository.findByContact(memberRequestDto.getContact()).isPresent())
            throw new AssociatedAccountExistsException();
        Member member = Member.of(memberRequestDto);
        member.setContact(removeHyphensFromContact(member.getContact()));
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberRepository.save(member);
    }

    public List<MemberResponseDto> getMembers() throws PermissionDeniedException {
        checkMemberAdminRole();
        List<Member> members = memberRepository.findAll();
        List<MemberResponseDto> memberResponseDtos = new ArrayList<>();
        for (Member member : members) {
            MemberResponseDto memberResponseDto = MemberResponseDto.of(member);
            memberResponseDtos.add(memberResponseDto);
        }
        return memberResponseDtos;
    }

    public MemberResponseDto searchMember(String memberId, String name) throws PermissionDeniedException {
        checkMemberAdminRole();
        Member member = null;
        if (memberId != null)
            member = getMemberByIdOrThrow(memberId);
        else if (name != null)
            member = getMemberByNameOrThrow(name);
        else
            throw new IllegalArgumentException("적어도 memberId 또는 name 중 하나를 제공해야 합니다.");

        if (member == null)
            throw new SearchResultNotExistException("검색 결과가 존재하지 않습니다.");
        return MemberResponseDto.of(member);
    }

    public void updateMemberInfoByMember(MemberUpdateRequestDto memberUpdateRequestDto) {
        String memberId = AuthUtil.getAuthenticationInfoMemberId();
        Member member = memberRepository.findById(memberId).get();
        member.setPassword(memberUpdateRequestDto.getPassword());
        member.setName(memberUpdateRequestDto.getName());
        member.setContact(memberUpdateRequestDto.getContact());
        member.setEmail(memberUpdateRequestDto.getEmail());
        member.setDepartment(memberUpdateRequestDto.getDepartment());
        member.setGrade(memberUpdateRequestDto.getGrade());
        member.setBirth(memberUpdateRequestDto.getBirth());
        member.setAddress(memberUpdateRequestDto.getAddress());
        member.setIsInSchool(memberUpdateRequestDto.getIsInSchool());
        member.setImageUrl(memberUpdateRequestDto.getImageUrl());
        memberRepository.save(member);
    }

    public void deleteMemberByAdmin(String memberId) throws PermissionDeniedException {
        checkMemberAdminRole();
        getMemberByIdOrThrow(memberId);
        memberRepository.deleteById(memberId);
    }

    public void deleteMemberByMember() {
        String memberId = AuthUtil.getAuthenticationInfoMemberId();
        memberRepository.deleteById(memberId);
    }

    public void checkMemberAdminRole() throws PermissionDeniedException {
        String memberId = AuthUtil.getAuthenticationInfoMemberId();
        Member member = memberRepository.findById(memberId).get();
        if (!member.getRole().equals(Role.ADMIN)) {
            throw new PermissionDeniedException("권한이 부족합니다.");
        }
    }

    public Member getMemberByIdOrThrow(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("해당 유저가 없습니다."));
    }

    public Member getMemberByNameOrThrow(String name) {
        return memberRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("해당 유저가 없습니다."));
    }

    public String removeHyphensFromContact(String contact) {
        return contact.replaceAll("-", "");
    }

    public static Member getCurrentMember(MemberRepository memberRepository){
        String memberId = AuthUtil.getAuthenticationInfoMemberId();
        return memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException("해당 멤버가 없습니다."));
    }
}
