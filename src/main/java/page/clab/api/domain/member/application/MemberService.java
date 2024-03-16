package page.clab.api.domain.member.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
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
import page.clab.api.global.common.verificationCode.application.VerificationCodeService;
import page.clab.api.global.common.verificationCode.domain.VerificationCode;
import page.clab.api.global.common.verificationCode.dto.request.VerificationCodeRequestDto;
import page.clab.api.global.exception.InvalidInformationException;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Autowired
    public void setEmailService(@Lazy EmailService emailService) {
        this.emailService = emailService;
    }

    @Transactional
    public String createMember(MemberRequestDto memberRequestDto) {
        checkMemberUniqueness(memberRequestDto);
        Member member = Member.of(memberRequestDto);
        member.setContact(removeHyphensFromContact(member.getContact()));
        setupMemberPassword(member);
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

    public PagedResponseDto<MemberResponseDto> getMembersByConditions(String id, String name, Pageable pageable) {
        Page<Member> members = memberRepository.findByConditions(id, name, pageable);
        return new PagedResponseDto<>(members.map(MemberResponseDto::of));
    }

    public PagedResponseDto<MemberBirthdayResponseDto> getBirthdaysThisMonth(int month, Pageable pageable) {
        Page<Member> birthdayMembers = memberRepository.findBirthdaysThisMonth(month, pageable);
        List<MemberBirthdayResponseDto> birthdayResponseDtos = birthdayMembers.getContent().stream()
                .map(MemberBirthdayResponseDto::of)
                .collect(Collectors.toList());
        return new PagedResponseDto<>(new PageImpl<>(birthdayResponseDtos, pageable, birthdayResponseDtos.size()));
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
        emailService.sendPasswordResetEmail(member, code);
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

    public Member getMemberByIdOrThrowLoginFailed(String memberId) throws LoginFaliedException {
        return memberRepository.findById(memberId)
                .orElseThrow(LoginFaliedException::new);
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

    public Member getMemberByEmail(String email) {
        return (Member)memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("해당 이메일을 사용하는 멤버가 없습니다."));
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    private void setupMemberPassword(Member member) {
        if (member.getPassword().isEmpty()) {
            setRandomPasswordAndSendEmail(member);
        } else {
            member.setPassword(passwordEncoder.encode(member.getPassword()));
        }
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
                emailService.broadcastEmailToApprovedMember(member, password);
            } catch (Exception e) {
                log.error("이메일 전송 실패: {}", e.getMessage());
            }
        });
    }

    private void checkMemberUniqueness(MemberRequestDto memberRequestDto) {
        if (memberRepository.findById(memberRequestDto.getId()).isPresent())
            throw new AssociatedAccountExistsException("이미 사용 중인 아이디입니다.");
        if (memberRepository.findByContact(memberRequestDto.getContact()).isPresent())
            throw new AssociatedAccountExistsException("이미 사용 중인 연락처입니다.");
        if (memberRepository.findByEmail(memberRequestDto.getEmail()).isPresent())
            throw new AssociatedAccountExistsException("이미 사용 중인 이메일입니다.");
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
