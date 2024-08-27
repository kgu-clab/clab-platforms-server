package page.clab.api.domain.memberManagement.workExperience.application.event;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.hiring.application.domain.Application;
import page.clab.api.domain.memberManagement.member.application.event.MemberEventProcessor;
import page.clab.api.domain.memberManagement.member.application.event.MemberEventProcessorRegistry;
import page.clab.api.domain.memberManagement.workExperience.application.port.out.RegisterWorkExperiencePort;
import page.clab.api.domain.memberManagement.workExperience.application.port.out.RetrieveWorkExperiencePort;
import page.clab.api.domain.memberManagement.workExperience.domain.WorkExperience;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WorkExperienceEventProcessor implements MemberEventProcessor {

    private final RegisterWorkExperiencePort registerWorkExperiencePort;
    private final RetrieveWorkExperiencePort retrieveWorkExperiencePort;
    private final MemberEventProcessorRegistry processorRegistry;

    @PostConstruct
    public void init() {
        processorRegistry.register(this);
    }

    @Override
    @Transactional
    public void processMemberDeleted(String memberId) {
        List<WorkExperience> workExperiences = retrieveWorkExperiencePort.findByMemberId(memberId);
        workExperiences.forEach(WorkExperience::delete);
        registerWorkExperiencePort.saveAll(workExperiences);
    }

    @Override
    public void processMemberUpdated(String memberId) {
        // do nothing
    }

    @Override
    public void processMemberCreated(Application application) {
        // do nothing
    }
}
