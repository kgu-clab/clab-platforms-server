package page.clab.api.domain.member.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.application.dao.ApplicationRepository;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.exception.NotApprovedApplicationException;
import page.clab.api.domain.member.dao.MemberRepository;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.dto.request.MemberRequestDto;
import page.clab.api.domain.member.dto.request.MemberResetPasswordRequestDto;
import page.clab.api.domain.member.dto.request.MemberUpdateRequestDto;
import page.clab.api.domain.member.dto.response.MemberBirthdayResponseDto;
import page.clab.api.domain.member.dto.response.MemberResponseDto;
import page.clab.api.domain.member.dto.response.MyProfileResponseDto;
import page.clab.api.domain.member.event.MemberDeletedEvent;
import page.clab.api.domain.member.event.MemberUpdatedEvent;
import page.clab.api.domain.member.exception.DuplicateMemberContactException;
import page.clab.api.domain.member.exception.DuplicateMemberEmailException;
import page.clab.api.domain.member.exception.DuplicateMemberIdException;
import page.clab.api.domain.position.dao.PositionRepository;
import page.clab.api.domain.position.domain.Position;
import page.clab.api.domain.position.domain.PositionType;
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

    private final MemberLookupService memberLookupService;

    private final VerificationService verificationService;

    private final ValidationService validationService;

    private final EmailService emailService;

    private final ApplicationRepository applicationRepository;

    private final PositionRepository positionRepository;

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final FileService fileService;

    private final ApplicationEventPublisher eventPublisher;

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

    @Transactional(readOnly = true)
    public PagedResponseDto<MemberResponseDto> getMembersByConditions(String id, String name, Pageable pageable) {
        Page<Member> members = memberRepository.findByConditions(id, name, pageable);
        return new PagedResponseDto<>(members.map(MemberResponseDto::toDto));
    }

    @Transactional(readOnly = true)
    public MyProfileResponseDto getMyProfile() {
        Member currentMember = memberLookupService.getCurrentMember();
        return MyProfileResponseDto.toDto(currentMember);
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<MemberBirthdayResponseDto> getBirthdaysThisMonth(int month, Pageable pageable) {
        Page<Member> birthdayMembers = memberRepository.findBirthdaysThisMonth(month, pageable);
        return new PagedResponseDto<>(birthdayMembers.map(MemberBirthdayResponseDto::toDto));
    }

    @Transactional
    public String updateMemberInfo(String memberId, MemberUpdateRequestDto requestDto) throws PermissionDeniedException {
        Member currentMember = memberLookupService.getCurrentMember();
        Member member = memberLookupService.getMemberByIdOrThrow(memberId);
        member.validateAccessPermission(currentMember);
        updateMember(requestDto, member);
        validationService.checkValid(member);
        memberRepository.save(member);
        eventPublisher.publishEvent(new MemberUpdatedEvent(this, member));
        return member.getId();
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
        Member member = memberLookupService.getMemberByIdOrThrow(requestDto.getMemberId());
        Verification verification = verificationService.validateVerificationCode(requestDto, member);
        updateMemberPasswordWithVerificationCode(verification.getVerificationCode(), member);
        return member.getId();
    }

    public String deleteMember(String memberId) {
        Member member = memberLookupService.getMemberByIdOrThrow(memberId);
        memberRepository.delete(member);
        eventPublisher.publishEvent(new MemberDeletedEvent(this, member));
        return member.getId();
    }

    private Application getApplicationByRecruitmentIdAndStudentIdOrThrow(Long recruitmentId, String memberId) {
        return applicationRepository.findByRecruitmentIdAndStudentId(recruitmentId, memberId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 지원서입니다."));
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
        if (memberRepository.existsById(requestDto.getId()))
            throw new DuplicateMemberIdException("이미 사용 중인 아이디입니다.");
        if (memberRepository.existsByContact(requestDto.getContact()))
            throw new DuplicateMemberContactException("이미 사용 중인 연락처입니다.");
        if (memberRepository.existsByEmail(requestDto.getEmail()))
            throw new DuplicateMemberEmailException("이미 사용 중인 이메일입니다.");
    }

    public void createPositionByMember(Member member) {
        if (positionRepository.findByMemberIdAndYearAndPositionType(member.getId(), String.valueOf(LocalDate.now().getYear()), PositionType.MEMBER).isPresent()) {
            return;
        }
        Position position = Position.create(member.getId());
        positionRepository.save(position);
    }

    private Member validateResetPasswordRequest(MemberResetPasswordRequestDto requestDto) {
        Member member = memberLookupService.getMemberByIdOrThrow(requestDto.getId());
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
        if (requestDto.getImageUrl() != null && requestDto.getImageUrl().isEmpty()) {
            member.clearImageUrl();
            fileService.deleteFile(DeleteFileRequestDto.create(previousImageUrl));
        }
    }

}
