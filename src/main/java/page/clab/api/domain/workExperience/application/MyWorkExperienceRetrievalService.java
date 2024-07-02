package page.clab.api.domain.workExperience.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.domain.workExperience.application.port.in.MyWorkExperienceRetrievalUseCase;
import page.clab.api.domain.workExperience.application.port.out.RetrieveMyWorkExperiencePort;
import page.clab.api.domain.workExperience.domain.WorkExperience;
import page.clab.api.domain.workExperience.dto.response.WorkExperienceResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class MyWorkExperienceRetrievalService implements MyWorkExperienceRetrievalUseCase {

    private final MemberLookupUseCase memberLookupUseCase;
    private final RetrieveMyWorkExperiencePort retrieveMyWorkExperiencePort;

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<WorkExperienceResponseDto> retrieve(Pageable pageable) {
        String currentMemberId = memberLookupUseCase.getCurrentMemberId();
        Page<WorkExperience> workExperiences = retrieveMyWorkExperiencePort.findByMemberId(currentMemberId, pageable);
        return new PagedResponseDto<>(workExperiences.map(WorkExperienceResponseDto::toDto));
    }
}
