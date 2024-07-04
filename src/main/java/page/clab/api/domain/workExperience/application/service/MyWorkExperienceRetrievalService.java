package page.clab.api.domain.workExperience.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.domain.workExperience.application.port.in.RetrieveMyWorkExperienceUseCase;
import page.clab.api.domain.workExperience.application.port.out.RetrieveWorkExperiencePort;
import page.clab.api.domain.workExperience.domain.WorkExperience;
import page.clab.api.domain.workExperience.dto.response.WorkExperienceResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class MyWorkExperienceRetrievalService implements RetrieveMyWorkExperienceUseCase {

    private final RetrieveMemberUseCase retrieveMemberUseCase;
    private final RetrieveWorkExperiencePort retrieveWorkExperiencePort;

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<WorkExperienceResponseDto> retrieveMyWorkExperience(Pageable pageable) {
        String currentMemberId = retrieveMemberUseCase.getCurrentMemberId();
        Page<WorkExperience> workExperiences = retrieveWorkExperiencePort.findByMemberId(currentMemberId, pageable);
        return new PagedResponseDto<>(workExperiences.map(WorkExperienceResponseDto::toDto));
    }
}
