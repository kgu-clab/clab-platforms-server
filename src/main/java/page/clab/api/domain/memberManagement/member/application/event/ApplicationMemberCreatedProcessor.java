package page.clab.api.domain.memberManagement.member.application.event;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import page.clab.api.domain.hiring.application.application.dto.request.ApplicationMemberCreationDto;
import page.clab.api.domain.hiring.application.application.event.ApplicationEventProcessor;
import page.clab.api.domain.hiring.application.application.event.ApplicationEventProcessorRegistry;
import page.clab.api.domain.memberManagement.member.application.port.in.ManageMemberPasswordUseCase;
import page.clab.api.domain.memberManagement.member.application.port.out.RegisterMemberPort;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.global.common.email.application.EmailService;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationMemberCreatedProcessor implements ApplicationEventProcessor {

    private final RegisterMemberPort registerMemberPort;
    private final ManageMemberPasswordUseCase manageMemberPasswordUseCase;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventProcessorRegistry processorRegistry;

    @PostConstruct
    public void init() {
        processorRegistry.register(this);
    }

    @Override
    public void processApplicationMemberCreated(ApplicationMemberCreationDto dto) {
        Member member = Member.toMember(dto);
        String finalPassword = manageMemberPasswordUseCase.generateOrRetrievePassword(member.getPassword());
        member.updatePassword(finalPassword, passwordEncoder);
        registerMemberPort.save(member);
        emailService.broadcastEmailToApprovedMember(member, finalPassword);
    }

    @Override
    public void processPositionCreated(String memberId) {
        // do nothing
    }
}
