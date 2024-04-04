package page.clab.api.domain.member.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.application.dao.ApplicationRepository;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.exception.NotApprovedApplicationException;
import page.clab.api.domain.login.exception.LoginFaliedException;
import page.clab.api.domain.member.dao.MemberRepository;
import page.clab.api.domain.member.domain.Member;
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
import page.clab.api.global.common.file.application.FileService;
import page.clab.api.global.common.file.dto.request.DeleteFileRequestDto;
import page.clab.api.global.common.verification.application.VerificationService;
import page.clab.api.global.common.verification.domain.Verification;
import page.clab.api.global.common.verification.dto.request.VerificationRequestDto;
import page.clab.api.global.exception.InvalidInformationException;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final VerificationService verificationService;

    private final ValidationService validationService;

    private EmailService emailService;

    private final ApplicationRepository applicationRepository;

    private final PositionRepository positionRepository;

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private  FileService fileService;

    @Autowired
    public void setEmailService(@Lazy EmailService emailService) {
        this.emailService = emailService;
    }

    @Autowired
    public void setFileServie(@Lazy FileService fileService) {
        this.fileService = fileService;
    }

    @Transactional
    public String createMember(MemberRequestDto requestDto) {
        checkMemberUniqueness(requestDto);
        Member member = MemberRequestDto.toEntity(requestDto);
        validationService.checkValid(member);
        setupMemberPassword(member);
        memberRepository.save(member);
        createPositionByMember(member);
        return member.getId();
    }

    @Transactional
    public List<String> createMembersByRecruitmentId(Long recruitmentId) {
        List<Application> applications = applicationRepository.findByRecruitmentIdAndIsPass(recruitmentId, true);
        return applications.stream()
                .map(this::createMemberFromApplication)
                .toList();
    }

    @Transactional
    public String createMemberByRecruitmentId(Long recruitmentId, String memberId) {
        Application application = getApplicationByRecruitmentIdAndStudentIdOrThrow(recruitmentId, memberId);
        return createMemberFromApplication(application);
    }

    public List<MemberResponseDto> getMembers() {
        List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(MemberResponseDto::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<MemberResponseDto> getMembersByConditions(String id, String name, Pageable pageable) {
        Page<Member> members = memberRepository.findByConditions(id, name, pageable);
        return new PagedResponseDto<>(members.map(MemberResponseDto::toDto));
    }

    @Transactional(readOnly = true)
    public MyProfileResponseDto getMyProfile() {
        Member currentMember = getCurrentMember();
        return MyProfileResponseDto.toDto(currentMember);
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<MemberBirthdayResponseDto> getBirthdaysThisMonth(int month, Pageable pageable) {
        Page<Member> birthdayMembers = memberRepository.findBirthdaysThisMonth(month, pageable);
        return new PagedResponseDto<>(birthdayMembers.map(MemberBirthdayResponseDto::toDto));
    }

    @Transactional
    public String updateMemberInfo(String memberId, MemberUpdateRequestDto requestDto) throws PermissionDeniedException {
        Member currentMember = getCurrentMember();
        Member member = getMemberByIdOrThrow(memberId);
        member.validateAccessPermission(currentMember);
        updateMember(requestDto, member);
        validationService.checkValid(member);
        return memberRepository.save(member).getId();
    }

    @Transactional
    public String requestResetMemberPassword(MemberResetPasswordRequestDto requestDto) {
        Member member = validateResetPasswordRequest(requestDto);
        String code = verificationService.generateVerificationCode();
        verificationService.saveVerificationCode(member.getId(), code);
        emailService.sendPasswordResetEmail(member, code);
        return member.getId();
    }

    @Transactional
    public String verifyResetMemberPassword(VerificationRequestDto requestDto) {
        Member member = getMemberByIdOrThrow(requestDto.getMemberId());
        Verification verification = verificationService.validateVerificationCode(requestDto, member);
        updateMemberPasswordWithVerificationCode(verification.getVerificationCode(), member);
        return member.getId();
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
            member.updatePassword(member.getPassword(), passwordEncoder);
        }
    }

    private String createMemberFromApplication(Application application) {
        if (!application.getIsPass()) {
            throw new NotApprovedApplicationException("승인되지 않은 지원서입니다.");
        }
        Member member = createMemberByApplication(application);
        createPositionByMember(member);
        return member.getId();
    }

    private Member createMemberByApplication(Application application) {
        Member member = Application.toMember(application);
        validationService.checkValid(member);
        Member existingMember = memberRepository.findById(member.getId()).orElse(null);
        if (existingMember != null) {
            return existingMember;
        }
        setRandomPasswordAndSendEmail(member);
        memberRepository.save(member);
        return member;
    }

    private void setRandomPasswordAndSendEmail(Member member) {
        String password = verificationService.generateVerificationCode();
        member.updatePassword(password, passwordEncoder);
        CompletableFuture.runAsync(() -> {
            try {
                emailService.broadcastEmailToApprovedMember(member, password);
            } catch (Exception e) {
                log.error("이메일 전송 실패: {}", e.getMessage());
            }
        });
    }

    private void checkMemberUniqueness(MemberRequestDto requestDto) {
        if (memberRepository.findById(requestDto.getId()).isPresent())
            throw new AssociatedAccountExistsException("이미 사용 중인 아이디입니다.");
        if (memberRepository.findByContact(requestDto.getContact()).isPresent())
            throw new AssociatedAccountExistsException("이미 사용 중인 연락처입니다.");
        if (memberRepository.findByEmail(requestDto.getEmail()).isPresent())
            throw new AssociatedAccountExistsException("이미 사용 중인 이메일입니다.");
    }

    public void createPositionByMember(Member member) {
        if (positionRepository.findByMemberAndYearAndPositionType(member, String.valueOf(LocalDate.now().getYear()), PositionType.MEMBER).isPresent()) {
            return;
        }
        Position position = Position.create(member);
        positionRepository.save(position);
    }

    private Member validateResetPasswordRequest(MemberResetPasswordRequestDto requestDto) {
        Member member = getMemberByIdOrThrow(requestDto.getId());
        if (!member.isSameName(requestDto.getName()) || !member.isSameEmail(requestDto.getEmail())) {
            throw new InvalidInformationException("올바르지 않은 정보입니다.");
        }
        return member;
    }

    private void updateMemberPasswordWithVerificationCode(String verificationCode, Member member) {
        member.updatePassword(verificationCode, passwordEncoder);
        verificationService.deleteVerificationCode(verificationCode);
    }

    private void updateMember(MemberUpdateRequestDto requestDto, Member member) throws PermissionDeniedException {
        String previousImageUrl = member.getImageUrl();
        member.update(requestDto, passwordEncoder);
        if (requestDto.getImageUrl().isEmpty()) {
            member.clearImageUrl();
            fileService.deleteFile(DeleteFileRequestDto.create(previousImageUrl));
        }
    }

    public List<Member> getAdmins() {
        return memberRepository.findAll()
                .stream()
                .filter(Member::isAdminRole)
                .toList();
    }

    public List<Member> getSuperAdmins() {
        return memberRepository.findAll()
                .stream()
                .filter(Member::isSuperAdminRole)
                .toList();
    }

}
