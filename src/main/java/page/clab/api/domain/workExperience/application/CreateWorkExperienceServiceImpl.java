package page.clab.api.domain.workExperience.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.workExperience.dao.WorkExperienceRepository;
import page.clab.api.domain.workExperience.domain.WorkExperience;
import page.clab.api.domain.workExperience.dto.request.WorkExperienceRequestDto;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class CreateWorkExperienceServiceImpl implements CreateWorkExperienceService {

    private final MemberLookupService memberLookupService;
    private final ValidationService validationService;
    private final WorkExperienceRepository workExperienceRepository;

    @Override
    @Transactional
    public Long createWorkExperience(WorkExperienceRequestDto requestDto) {
        String currentMemberId = memberLookupService.getCurrentMemberId();
        WorkExperience workExperience = WorkExperienceRequestDto.toEntity(requestDto, currentMemberId);
        validationService.checkValid(workExperience);
        return workExperienceRepository.save(workExperience).getId();
    }
}
