package page.clab.api.domain.workExperience.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.workExperience.application.FetchWorkExperiencesByConditionsService;
import page.clab.api.domain.workExperience.dao.WorkExperienceRepository;
import page.clab.api.domain.workExperience.domain.WorkExperience;
import page.clab.api.domain.workExperience.dto.response.WorkExperienceResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class FetchWorkExperiencesByConditionsServiceImpl implements FetchWorkExperiencesByConditionsService {

    private final WorkExperienceRepository workExperienceRepository;

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<WorkExperienceResponseDto> fetchWorkExperiencesByConditions(String memberId, Pageable pageable) {
        Page<WorkExperience> workExperiences = workExperienceRepository.findByMemberId(memberId, pageable);
        return new PagedResponseDto<>(workExperiences.map(WorkExperienceResponseDto::toDto));
    }
}
