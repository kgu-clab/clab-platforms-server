package page.clab.api.domain.workExperience.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.domain.workExperience.application.dto.request.WorkExperienceRequestDto;
import page.clab.api.domain.workExperience.application.port.in.RegisterWorkExperienceUseCase;
import page.clab.api.domain.workExperience.application.port.out.RegisterWorkExperiencePort;
import page.clab.api.domain.workExperience.domain.WorkExperience;

@Service
@RequiredArgsConstructor
public class WorkExperienceRegisterService implements RegisterWorkExperienceUseCase {

    private final RetrieveMemberUseCase retrieveMemberUseCase;
    private final RegisterWorkExperiencePort registerWorkExperiencePort;

    @Override
    @Transactional
    public Long registerWorkExperience(WorkExperienceRequestDto requestDto) {
        String currentMemberId = retrieveMemberUseCase.getCurrentMemberId();
        WorkExperience workExperience = WorkExperienceRequestDto.toEntity(requestDto, currentMemberId);
        workExperience.validateBusinessRules();
        return registerWorkExperiencePort.save(workExperience).getId();
    }
}
