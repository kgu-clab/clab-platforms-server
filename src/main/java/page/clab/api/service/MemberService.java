package page.clab.api.service;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
import page.clab.api.type.dto.NotificationRequestDto;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.entity.Member;
import page.clab.api.type.etc.MemberStatus;
import page.clab.api.type.etc.Role;
import page.clab.api.util.FileSystemUtil;

@Service
@RequiredArgsConstructor
public class MemberService {

    private NotificationService notificationService;

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${resource.file.path}")
    private String filePath;

    @Autowired
    public void setNotificationService(@Lazy NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public String createMember(MemberRequestDto memberRequestDto) throws PermissionDeniedException {
        checkMemberAdminRole();
        if (memberRepository.findById(memberRequestDto.getId()).isPresent())
            throw new AssociatedAccountExistsException("이미 사용 중인 아이디입니다.");
        if (memberRepository.findByContact(memberRequestDto.getContact()).isPresent())
            throw new AssociatedAccountExistsException("이미 사용 중인 연락처입니다.");
        if (memberRepository.findByEmail(memberRequestDto.getEmail()).isPresent())
            throw new AssociatedAccountExistsException("이미 사용 중인 이메일입니다.");
        Member member = Member.of(memberRequestDto);
        member.setContact(removeHyphensFromContact(member.getContact()));
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        return memberRepository.save(member).getId();
    }

    public List<MemberResponseDto> getMembers() throws PermissionDeniedException {
        checkMemberAdminRole();
        List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(MemberResponseDto::of)
                .collect(Collectors.toList());
    }

    public PagedResponseDto<MemberResponseDto> getMembers(Pageable pageable) throws PermissionDeniedException {
        checkMemberAdminRole();
        Page<Member> members = memberRepository.findAllByOrderByCreatedAtDesc(pageable);
        return new PagedResponseDto<>(members.map(MemberResponseDto::of));
    }

    public PagedResponseDto<MemberResponseDto> getBirthdaysThisMonth(String month, Pageable pageable) {
        LocalDate currentMonth = LocalDate.now().withMonth(Integer.parseInt(month));
        List<Member> members = memberRepository.findAll();
        List<Member> birthdayMembers = members.stream()
                .filter(member -> member.getBirth().getMonth() == currentMonth.getMonth())
                .collect(Collectors.toList());
        birthdayMembers.sort(Comparator.comparing(member -> member.getBirth().getDayOfMonth()));
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), birthdayMembers.size());
        Page<Member> birthdayMembersPage = new PageImpl<>(birthdayMembers.subList(start, end), pageable, birthdayMembers.size());
        return new PagedResponseDto<>(birthdayMembersPage.map(MemberResponseDto::of));
    }

    public PagedResponseDto<MemberResponseDto> searchMember(String memberId, String name, MemberStatus memberStatus, Pageable pageable) throws PermissionDeniedException {
        checkMemberAdminRole();
        Page<Member> members;
        if (memberId != null) {
            Member member = getMemberByIdOrThrow(memberId);
            members = new PageImpl<>(Arrays.asList(member), pageable, 1);
        } else if (name != null) {
            members = getMemberByName(name, pageable);
        } else if (memberStatus != null) {
            members = getMemberByMemberStatus(memberStatus, pageable);
        } else {
            throw new IllegalArgumentException("적어도 memberId, name, memberStatus 중 하나를 제공해야 합니다.");
        }
        if (members.isEmpty()) {
            throw new SearchResultNotExistException("검색 결과가 존재하지 않습니다.");
        }
        return new PagedResponseDto<>(members.map(MemberResponseDto::of));
    }

    public String updateMemberInfo(String memberId, MemberRequestDto memberRequestDto) throws PermissionDeniedException {
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
        return memberRepository.save(updatedMember).getId();
    }

    @Transactional
    public String updateMemberStatusByAdmin(String memberId, MemberStatus memberStatus) throws PermissionDeniedException {
        checkMemberAdminRole();
        Member member = getMemberByIdOrThrow(memberId);
        member.setMemberStatus(memberStatus);
        String id = memberRepository.save(member).getId();

        NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                .memberId(member.getId())
                .content("관리자가 " + member.getName() + "님의 회원 상태를 [" + memberStatus.getDescription() + "]으로 변경하였습니다.")
                .build();
        notificationService.createNotification(notificationRequestDto);
        return id;
    }

    public PagedResponseDto<CloudUsageInfo> getAllCloudUsages(Pageable pageable) throws PermissionDeniedException {
        checkMemberAdminRole();
        Page<Member> members = memberRepository.findAllByOrderByCreatedAtDesc(pageable);
        return new PagedResponseDto<>(members.map(member -> getCloudUsageByMemberId(member.getId())));
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

    public PagedResponseDto<FileInfo> getFilesInMemberDirectory(String memberId, Pageable pageable) {
        Member currentMember = getCurrentMember();
        Member member = getMemberByIdOrThrow(memberId);
        if (!(isMemberAdminRole(member) || currentMember.getId().equals(memberId))) {
            return null;
        }
        File directory = new File(filePath + "/members/" + memberId);
        File[] files = FileSystemUtil.getFilesInDirectory(directory).toArray(new File[0]);
        if (files.length == 0) {
            return null;
        }
        int totalFiles = files.length;
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), totalFiles);
        if (start >= totalFiles) {
            return null;
        }
        Page<FileInfo> fileInfoPage = new PageImpl<>(Arrays.stream(files)
                .map(FileInfo::of)
                .collect(Collectors.toList()).subList(start, end), pageable, totalFiles);
        return new PagedResponseDto<>(fileInfoPage);
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
        if (!member.getRole().equals(Role.USER)) {
            return true;
        }
        return false;
    }

    public Member getMemberById(String memberId) {
        return memberRepository.findById(memberId)
                .orElse(null);
    }

    public Member getMemberByIdOrThrow(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("해당 멤버가 없습니다."));
    }

    public Page<Member> getMemberByName(String name, Pageable pageable) {
        return memberRepository.findAllByNameOrderByCreatedAtDesc(name, pageable);
    }

    public Page<Member> getMemberByMemberStatus(MemberStatus memberStatus, Pageable pageable) {
        return memberRepository.findByMemberStatusOrderByCreatedAtDesc(memberStatus, pageable);
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

    public List<Member> getMembersByRole(Role role) {
        return memberRepository.findAllByRole(role);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

}