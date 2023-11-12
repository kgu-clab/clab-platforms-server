package page.clab.api.service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import page.clab.api.auth.util.AuthUtil;
import page.clab.api.exception.AssociatedAccountExistsException;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.exception.SearchResultNotExistException;
import page.clab.api.repository.MemberRepository;
import page.clab.api.type.dto.CloudUsageInfo;
import page.clab.api.type.dto.FileInfo;
import page.clab.api.type.dto.MemberRequestDto;
import page.clab.api.type.dto.MemberResponseDto;
import page.clab.api.type.entity.Member;
import page.clab.api.type.etc.MemberStatus;
import page.clab.api.type.etc.Role;
import page.clab.api.util.FileSystemUtil;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${resource.file.path}")
    private String filePath;

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
        return members.stream()
                .map(MemberResponseDto::of)
                .collect(Collectors.toList());
    }

    public List<MemberResponseDto> searchMember(String memberId, String name, MemberStatus memberStatus) throws PermissionDeniedException {
        checkMemberAdminRole();
        List<Member> members = new ArrayList<>();
        if (memberId != null) {
            members.add(getMemberByIdOrThrow(memberId));
        } else if (name != null) {
            members.addAll(getMemberByName(name));
        } else if (memberStatus != null) {
            members.addAll(getMemberByMemberStatus(memberStatus));
        } else {
            throw new IllegalArgumentException("적어도 memberId, name, memberStatus 중 하나를 제공해야 합니다.");
        }
        if (members.isEmpty()) {
            throw new SearchResultNotExistException("검색 결과가 존재하지 않습니다.");
        }
        return members.stream()
                .map(MemberResponseDto::of)
                .collect(Collectors.toList());
    }

    public void updateMemberInfo(String memberId, MemberRequestDto memberRequestDto) throws PermissionDeniedException {
        Member currentMember = getCurrentMember();
        Member member = getMemberByIdOrThrow(memberId);
        if (!(member.getId().equals(currentMember.getId()) || isMemberAdminRole(currentMember))) {
            throw new PermissionDeniedException("멤버 수정 권한이 부족합니다.");
        }
        Member updatedMember = Member.of(memberRequestDto);
        updatedMember.setMemberStatus(member.getMemberStatus());
        updatedMember.setRole(member.getRole());
        updatedMember.setProvider(member.getProvider());
        updatedMember.setLastLoginTime(member.getLastLoginTime());
        updatedMember.setLoanSuspensionDate(member.getLoanSuspensionDate());
        memberRepository.save(updatedMember);
    }

    public void updateMemberStatusByAdmin(String memberId, MemberStatus memberStatus) throws PermissionDeniedException {
        checkMemberAdminRole();
        Member member = getMemberByIdOrThrow(memberId);
        member.setMemberStatus(memberStatus);
        memberRepository.save(member);
    }

    public List<CloudUsageInfo> getAllCloudUsages() throws PermissionDeniedException {
        checkMemberAdminRole();
        List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(member -> getCloudUsageByMemberId(member.getId()))
                .collect(Collectors.toList());
    }

    public CloudUsageInfo getCloudUsageByMemberId(String memberId) {
        Member member = getMemberByIdOrThrow(memberId);
        File directory = new File(filePath + "/members/" + memberId);
        long usage = FileSystemUtil.calculateDirectorySize(directory);
        if (usage == -1) {
            throw new NotFoundException("올바르지 않은 접근입니다.");
        }
        CloudUsageInfo cloudUsageInfo = CloudUsageInfo.builder()
                .memberId(memberId)
                .usage(usage)
                .build();
        return cloudUsageInfo;
    }

    public List<FileInfo> getFilesInMemberDirectory(String memberId) {
        Member currentMember = getCurrentMember();
        Member member = getMemberByIdOrThrow(memberId);
        if (!(isMemberAdminRole(member) || currentMember.getId().equals(memberId))) {
            return new ArrayList<>();
        }
        File directory = new File(filePath + "/members/" + memberId);
        File[] files = FileSystemUtil.getFilesInDirectory(directory).toArray(new File[0]);
        return Arrays.stream(files)
                .map(FileInfo::of)
                .collect(Collectors.toList());
    }

    public void setLastLoginTime(String memberId) {
        Member member = getMemberByIdOrThrow(memberId);
        member.setLastLoginTime(LocalDateTime.now());
        memberRepository.save(member);
    }

    public void checkMemberAdminRole() throws PermissionDeniedException {
        String memberId = AuthUtil.getAuthenticationInfoMemberId();
        Member member = memberRepository.findById(memberId).get();
        if (member.getRole().equals(Role.USER)) {
            throw new PermissionDeniedException("권한이 부족합니다.");
        }
    }

    public boolean isMemberAdminRole(Member member) {
        if (member.getRole().equals(Role.USER)) {
            return false;
        }
        return true;
    }

    public Member getMemberByIdOrThrow(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("해당 멤버가 없습니다."));
    }

    public List<Member> getMemberByName(String name) {
        return memberRepository.findAllByName(name);
    }

    public List<Member> getMemberByMemberStatus(MemberStatus memberStatus) {
        return memberRepository.findByMemberStatus(memberStatus);
    }

    public Member saveMember(Member updatedMember) {
        return memberRepository.save(updatedMember);
    }

    public String removeHyphensFromContact(String contact) {
        return contact.replaceAll("-", "");
    }

    public Member getCurrentMember(){
        String memberId = AuthUtil.getAuthenticationInfoMemberId();
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("해당 멤버가 없습니다."));
    }

}
