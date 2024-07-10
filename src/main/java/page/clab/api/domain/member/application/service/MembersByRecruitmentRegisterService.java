package page.clab.api.domain.member.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.application.application.exception.NotApprovedApplicationException;
import page.clab.api.domain.application.application.port.out.RetrieveApplicationPort;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.member.application.port.in.RegisterMembersByRecruitmentUseCase;
import page.clab.api.domain.member.application.port.out.RegisterMemberPort;
import page.clab.api.domain.member.application.port.out.RetrieveMemberPort;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.position.application.port.out.RegisterPositionPort;
import page.clab.api.domain.position.application.port.out.RetrievePositionPort;
import page.clab.api.domain.position.domain.Position;
import page.clab.api.domain.position.domain.PositionType;
import page.clab.api.global.common.email.application.EmailService;
import page.clab.api.global.common.verification.application.VerificationService;
import page.clab.api.global.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class MembersByRecruitmentRegisterService implements RegisterMembersByRecruitmentUseCase {

    private final VerificationService verificationService;
    private final EmailService emailService;
    private final RetrieveMemberPort retrieveMemberPort;
    private final RegisterMemberPort registerMemberPort;
    private final RegisterPositionPort registerPositionPort;
    private final RetrievePositionPort retrievePositionPort;
    private final RetrieveApplicationPort retrieveApplicationPort;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public List<String> registerMembersByRecruitment(Long recruitmentId) {
        List<Application> applications = retrieveApplicationPort.findByRecruitmentIdAndIsPass(recruitmentId, true);
        return applications.stream()
                .map(this::createMemberFromApplication)
                .toList();
    }

    @Transactional
    @Override
    public String registerMembersByRecruitment(Long recruitmentId, String memberId) {
        Application application = getApplicationByRecruitmentIdAndStudentIdOrThrow(recruitmentId, memberId);
        return createMemberFromApplication(application);
    }

    private Application getApplicationByRecruitmentIdAndStudentIdOrThrow(Long recruitmentId, String memberId) {
        return retrieveApplicationPort.findByRecruitmentIdAndStudentId(recruitmentId, memberId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 지원서입니다."));
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
        Member existingMember = retrieveMemberPort.findById(member.getId())
                .orElse(null);
        if (existingMember != null) {
            return existingMember;
        }
        setRandomPasswordAndSendEmail(member);
        registerMemberPort.save(member);
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

    public void createPositionByMember(Member member) {
        if (retrievePositionPort.findByMemberIdAndYearAndPositionType
                (member.getId(), String.valueOf(LocalDate.now().getYear()),PositionType.MEMBER).isPresent()) {
            return;
        }
        Position position = Position.create(member.getId());
        registerPositionPort.save(position);
    }
}
