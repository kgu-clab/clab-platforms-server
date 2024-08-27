package page.clab.api.domain.memberManagement.member.application.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.hiring.application.application.event.ApplicationApprovedEvent;
import page.clab.api.domain.hiring.application.domain.Application;
import page.clab.api.domain.memberManagement.member.application.port.out.RegisterMemberPort;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.global.common.email.application.EmailService;
import page.clab.api.global.common.verification.application.VerificationService;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberCreatedByApplicationDispatcher {

    private final RegisterMemberPort registerMemberPort;
    private final VerificationService verificationService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @EventListener
    @Transactional
    public void handleApplicationApprovedEvent(ApplicationApprovedEvent event) {
        Application application = event.getApplication();
        Member member = Member.fromApplication(application);
        setRandomPasswordAndSendEmail(member);
        registerMemberPort.save(member);

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
}
