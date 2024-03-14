package page.clab.api.domain.member.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import page.clab.api.domain.application.dao.ApplicationRepository;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.exception.NotApprovedApplicationException;
import page.clab.api.domain.login.exception.LoginFaliedException;
import page.clab.api.domain.member.dao.MemberRepository;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.domain.Role;
import page.clab.api.domain.member.dto.request.MemberRequestDto;
import page.clab.api.domain.member.dto.request.MemberResetPasswordRequestDto;
import page.clab.api.domain.member.dto.request.MemberUpdateRequestDto;
import page.clab.api.domain.member.dto.response.CloudUsageInfo;
import page.clab.api.domain.member.dto.response.MemberBirthdayResponseDto;
import page.clab.api.domain.member.dto.response.MemberResponseDto;
import page.clab.api.domain.member.dto.response.MyProfileResponseDto;
import page.clab.api.domain.member.exception.AssociatedAccountExistsException;
import page.clab.api.domain.position.dao.PositionRepository;
import page.clab.api.domain.position.domain.Position;
import page.clab.api.domain.position.domain.PositionType;
import page.clab.api.global.auth.util.AuthUtil;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.email.application.EmailService;
import page.clab.api.global.common.email.domain.EmailTemplateType;
import page.clab.api.global.common.email.dto.request.EmailDto;
import page.clab.api.global.common.file.dto.response.FileInfo;
import page.clab.api.global.common.verificationCode.application.VerificationCodeService;
import page.clab.api.global.common.verificationCode.domain.VerificationCode;
import page.clab.api.global.common.verificationCode.dto.request.VerificationCodeRequestDto;
import page.clab.api.global.exception.InvalidInformationException;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.exception.SearchResultNotExistException;
import page.clab.api.global.util.FileSystemUtil;

import java.io.File;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final VerificationCodeService verificationCodeService;

    private final ApplicationRepository applicationRepository;

    private final PositionRepository positionRepository;

    private EmailService emailService;

    @Value("${resource.file.path}")
    private String filePath;

    @Autowired
    public void setNotificationService(@Lazy EmailService emailService) {
        this.emailService = emailService;
    }

    @Transactional
    public String createMember(MemberRequestDto memberRequestDto) {
        if (memberRepository.findById(memberRequestDto.getId()).isPresent())
            throw new AssociatedAccountExistsException("이미 사용 중인 아이디입니다.");
        if (memberRepository.findByContact(memberRequestDto.getContact()).isPresent())
            throw new AssociatedAccountExistsException("이미 사용 중인 연락처입니다.");
        if (memberRepository.findByEmail(memberRequestDto.getEmail()).isPresent())
            throw new AssociatedAccountExistsException("이미 사용 중인 이메일입니다.");
        Member member = Member.of(memberRequestDto);
        member.setContact(removeHyphensFromContact(member.getContact()));
        if (member.getPassword().isEmpty()) {
            setRandomPasswordAndSendEmail(member);
        } else {
            member.setPassword(passwordEncoder.encode(member.getPassword()));
        }
        String id = memberRepository.save(member).getId();
        createPositionByMember(member);
        return id;
    }

    @Transactional
    public List<String> createMembersByRecruitmentId(Long recruitmentId) {
        List<Application> applications = applicationRepository.findByRecruitmentIdAndIsPass(recruitmentId, true);
        return applications.stream()
                .map(application -> {
                    Member member = createMemberByApplication(application);
                    createPositionByMember(member);
                    return member.getId();
                })
                .toList();
    }

    @Transactional
    public String createMemberByRecruitmentId(Long recruitmentId, String memberId) {
        Application application = getApplicationByRecruitmentIdAndStudentIdOrThrow(recruitmentId, memberId);
        if (!application.getIsPass()) {
            throw new NotApprovedApplicationException("승인되지 않은 지원서입니다.");
        }
        Member member = createMemberByApplication(application);
        createPositionByMember(member);
        return member.getId();
    }

    public List<MemberResponseDto> getMembers() {
        List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(MemberResponseDto::of)
                .collect(Collectors.toList());
    }

    public PagedResponseDto<MemberResponseDto> getMembers(Pageable pageable) {
        Page<Member> members = memberRepository.findAllByOrderByCreatedAtDesc(pageable);
        return new PagedResponseDto<>(members.map(MemberResponseDto::of));
    }

    public PagedResponseDto<MemberBirthdayResponseDto> getBirthdaysThisMonth(int month, Pageable pageable) {
        LocalDate currentMonth = LocalDate.now().withMonth(month);
        List<Member> members = memberRepository.findAll();
        List<Member> birthdayMembers = members.stream()
                .filter(member -> member.getBirth().getMonth() == currentMonth.getMonth())
                .collect(Collectors.toList());
        birthdayMembers.sort(Comparator.comparing(member -> member.getBirth().getDayOfMonth()));
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), birthdayMembers.size());
        Page<Member> birthdayMembersPage = new PageImpl<>(birthdayMembers.subList(start, end), pageable, birthdayMembers.size());
        return new PagedResponseDto<>(birthdayMembersPage.map(MemberBirthdayResponseDto::of));
    }

    public PagedResponseDto<MemberResponseDto> searchMember(String memberId, String name, Pageable pageable) {
        Page<Member> members;
        if (memberId != null) {
            Member member = getMemberByIdOrThrow(memberId);
            members = new PageImpl<>(Arrays.asList(member), pageable, 1);
        } else if (name != null) {
            members = getMemberByName(name, pageable);
        } else {
            throw new IllegalArgumentException("적어도 memberId, name 중 하나를 제공해야 합니다.");
        }
        if (members.isEmpty()) {
            throw new SearchResultNotExistException("검색 결과가 존재하지 않습니다.");
        }
        return new PagedResponseDto<>(members.map(MemberResponseDto::of));
    }

    public String updateMemberInfo(String memberId, MemberUpdateRequestDto memberUpdateRequestDto) throws PermissionDeniedException {
        Member currentMember = getCurrentMember();
        Member member = getMemberByIdOrThrow(memberId);
        if (!(member.getId().equals(currentMember.getId()) || isMemberSuperRole(currentMember))) {
            throw new PermissionDeniedException("멤버 수정 권한이 부족합니다.");
        }
        member.update(memberUpdateRequestDto, passwordEncoder);
        return memberRepository.save(member).getId();
    }

    @Transactional
    public void requestResetMemberPassword(MemberResetPasswordRequestDto memberResetPasswordRequestDto) {
        Member member = getMemberByIdOrThrow(memberResetPasswordRequestDto.getId());
        if (!(Objects.equals(member.getName(), memberResetPasswordRequestDto.getName())
                && Objects.equals(member.getEmail(), memberResetPasswordRequestDto.getEmail()))) {
            throw new InvalidInformationException("올바르지 않은 정보입니다.");
        }
        String code = generateVerificationCode();
        verificationCodeService.saveVerificationCode(member.getId(), code);
        emailService.broadcastEmail(
            new EmailDto(
                List.of(member.getEmail()),
                "C-Lab 비밀번호 재발급 인증 안내",
                "C-Lab 비밀번호 재발급 인증 안내 메일입니다.\n" +
                        "인증번호는 " + code + "입니다.\n",
                    EmailTemplateType.NORMAL
            ), null
        );
    }

    @Transactional
    public void verifyResetMemberPassword(VerificationCodeRequestDto verificationCodeRequestDto) {
        String memberId = verificationCodeRequestDto.getMemberId();
        Member member = getMemberByIdOrThrow(memberId);
        VerificationCode verificationCode = verificationCodeService.getVerificationCode(verificationCodeRequestDto.getVerificationCode());
        if (!verificationCode.getId().equals(memberId)) {
            throw new InvalidInformationException("올바르지 않은 인증 요청입니다.");
        }
        String newPassword = passwordEncoder.encode(verificationCode.getVerificationCode());
        member.setPassword(newPassword);
        memberRepository.save(member);
        verificationCodeService.deleteVerificationCode(verificationCodeRequestDto.getVerificationCode());
    }

    public PagedResponseDto<CloudUsageInfo> getAllCloudUsages(Pageable pageable) {
        Page<Member> members = memberRepository.findAllByOrderByCreatedAtDesc(pageable);
        return new PagedResponseDto<>(members.map(member -> {
            try {
                return getCloudUsageByMemberId(member.getId());
            } catch (PermissionDeniedException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    public CloudUsageInfo getCloudUsageByMemberId(String memberId) throws PermissionDeniedException {
        Member currentMember = getCurrentMember();
        if (!(memberId.equals(currentMember.getId()) || isMemberSuperRole(currentMember))) {
            throw new PermissionDeniedException("본인 또는 관리자만 접근 가능합니다.");
        }
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
        if (!(currentMember.getId().equals(memberId) || isMemberSuperRole(member))) {
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

    public MyProfileResponseDto getMyProfile() {
        Member currentMember = getCurrentMember();
        MyProfileResponseDto myProfileResponseDto = MyProfileResponseDto.of(currentMember);
        myProfileResponseDto.setRoleLevel(currentMember.getRole().toLong());
        return myProfileResponseDto;
    }

    public void setLastLoginTime(String memberId) {
        Member member = getMemberByIdOrThrow(memberId);
        member.setLastLoginTime(LocalDateTime.now());
        memberRepository.save(member);
    }

    public boolean isMemberAdminRole(Member member) {
        return (member.getRole().equals(Role.ADMIN) || member.getRole().equals(Role.SUPER));
    }

    public boolean isMemberSuperRole(Member member) {
        return member.getRole().equals(Role.SUPER);
    }

    public Member getMemberById(String memberId) {
        return memberRepository.findById(memberId)
                .orElse(null);
    }

    public Member getMemberByIdOrThrow(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("해당 멤버가 없습니다."));
    }

    private Application getApplicationByRecruitmentIdAndStudentIdOrThrow(Long recruitmentId, String memberId) {
        return applicationRepository.findByRecruitmentIdAndStudentId(recruitmentId, memberId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 지원서입니다."));
    }

    public Member getMemberByIdOrThrowLoginFaild(String memberId) throws LoginFaliedException {
        return memberRepository.findById(memberId)
                .orElseThrow(LoginFaliedException::new);
    }

    public Page<Member> getMemberByName(String name, Pageable pageable) {
        return memberRepository.findAllByNameOrderByCreatedAtDesc(name, pageable);
    }

    public Member saveMember(Member updatedMember) {
        return memberRepository.save(updatedMember);
    }

    public String removeHyphensFromContact(String contact) {
        return contact.replaceAll("-", "");
    }

    public Member getCurrentMember() {
        String memberId = AuthUtil.getAuthenticationInfoMemberId();
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("해당 멤버가 없습니다."));
    }

    public List<Member> getMembersByRole(Role role) {
        return memberRepository.findAllByRole(role);
    }

    public Member getMemberByEmail(String email) {
        return (Member)memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("해당 이메일을 사용하는 멤버가 없습니다."));
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public String generateVerificationCode() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] codeBytes = new byte[9];
        secureRandom.nextBytes(codeBytes);
        return Base64.encodeBase64URLSafeString(codeBytes);
    }

    private Member createMemberByApplication(Application application) {
        Member member = Member.of(application);
        Member existingMember = memberRepository.findById(member.getId()).orElse(null);
        if (existingMember != null) {
            return existingMember;
        }
        setRandomPasswordAndSendEmail(member);
        memberRepository.save(member);
        return member;
    }

    private void setRandomPasswordAndSendEmail(Member member) {
        String password = generateVerificationCode();
        member.setPassword(passwordEncoder.encode(password));
        CompletableFuture.runAsync(() -> {
            try {
                emailService.broadcastEmailToApprovedMember(
                        member,
                        new EmailDto(
                                List.of(member.getEmail()),
                                "C-Lab 계정 발급 안내",
                                "정식으로 C-Lab의 일원이 된 것을 축하드립니다.\n" +
                                        "C-Lab과 함께하는 동안 불타는 열정으로 모든 원하는 목표를 이루어 내시기를 바라고,\n" +
                                        "훗날, 당신의 합류가 C-Lab에겐 최고의 행운이었다고 기억되기를 희망합니다.\n\n" +
                                        "로그인을 위해 아래의 계정 정보를 확인해주세요.\n" +
                                        "ID: " + member.getId() + "\n" +
                                        "Password: " + password + "\n" +
                                        "로그인 후 비밀번호를 변경해주세요.",
                                EmailTemplateType.NORMAL
                        )
                );
            } catch (Exception e) {
                log.error("이메일 전송 실패: {}", e.getMessage());
            }
        });
    }

    public void createPositionByMember(Member member) {
        if (positionRepository.findByMemberAndYearAndPositionType(member, String.valueOf(LocalDate.now().getYear()), PositionType.MEMBER).isPresent()) {
            return;
        }
        Position position = Position.builder()
                .member(member)
                .positionType(PositionType.MEMBER)
                .year(String.valueOf(LocalDate.now().getYear()))
                .build();
        positionRepository.save(position);
    }

    public List<Member> getAdmins() {
        return memberRepository.findAll()
                .stream()
                .filter(member -> member.getRole().equals(Role.ADMIN) || member.getRole().equals(Role.SUPER))
                .toList();
    }

    public List<Member> getSuperAdmins() {
        return memberRepository.findAll()
                .stream()
                .filter(member -> member.getRole().equals(Role.SUPER))
                .toList();
    }

}
