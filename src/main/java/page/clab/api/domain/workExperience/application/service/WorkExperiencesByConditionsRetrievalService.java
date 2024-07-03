package page.clab.api.domain.workExperience.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.workExperience.application.port.in.RetrieveWorkExperiencesByConditionsUseCase;
import page.clab.api.domain.workExperience.application.port.out.RetrieveWorkExperienceByConditionsPort;
import page.clab.api.domain.workExperience.domain.WorkExperience;
import page.clab.api.domain.workExperience.dto.response.WorkExperienceResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class WorkExperiencesByConditionsRetrievalService implements RetrieveWorkExperiencesByConditionsUseCase {

    private final RetrieveWorkExperienceByConditionsPort retrieveWorkExperienceByConditionsPort;

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<WorkExperienceResponseDto> retrieve(String memberId, Pageable pageable) {
        Page<WorkExperience> workExperiences = retrieveWorkExperienceByConditionsPort.findByConditions(memberId, pageable);
        return new PagedResponseDto<>(workExperiences.map(WorkExperienceResponseDto::toDto));
    }
}
