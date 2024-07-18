package page.clab.api.domain.hiring.application.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.hiring.application.application.exception.NotApprovedApplicationException;
import page.clab.api.domain.hiring.application.application.port.in.RegisterMembersByRecruitmentUseCase;
import page.clab.api.domain.hiring.application.application.port.out.RetrieveApplicationPort;
import page.clab.api.domain.hiring.application.domain.Application;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.domain.memberManagement.position.domain.Position;
import page.clab.api.domain.memberManagement.position.domain.PositionType;
import page.clab.api.external.memberManagement.member.application.port.ExternalRegisterMemberUseCase;
import page.clab.api.external.memberManagement.member.application.port.ExternalRetrieveMemberUseCase;
import page.clab.api.external.memberManagement.position.application.port.ExternalRegisterPositionUseCase;
import page.clab.api.external.memberManagement.position.application.port.ExternalRetrievePositionUseCase;
import page.clab.api.global.common.email.application.EmailService;
import page.clab.api.global.common.verification.application.VerificationService;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationMemberRegisterService implements RegisterMembersByRecruitmentUseCase {

    private final RetrieveApplicationPort retrieveApplicationPort;
    private final ExternalRegisterMemberUseCase externalRegisterMemberUseCase;
    private final ExternalRetrieveMemberUseCase externalRetrieveMemberUseCase;
    private final ExternalRegisterPositionUseCase externalRegisterPositionUseCase;
    private final ExternalRetrievePositionUseCase externalRetrievePositionUseCase;
    private final EmailService emailService;
    private final VerificationService verificationService;
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
    public String registerMembersByRecruitment(Long recruitmentId, String studentId) {
        Application application = retrieveApplicationPort.findByRecruitmentIdAndStudentIdOrThrow(recruitmentId, studentId);
        return createMemberFromApplication(application);
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
        Member existingMember = externalRetrieveMemberUseCase.findById(member.getId())
                .orElse(null);
        if (existingMember != null) {
            return existingMember;
        }
        setRandomPasswordAndSendEmail(member);
        externalRegisterMemberUseCase.save(member);
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
        if (externalRetrievePositionUseCase.findByMemberIdAndYearAndPositionType
                (member.getId(), String.valueOf(LocalDate.now().getYear()), PositionType.MEMBER).isPresent()) {
            return;
        }
        Position position = Position.create(member.getId());
        externalRegisterPositionUseCase.save(position);
    }
}
