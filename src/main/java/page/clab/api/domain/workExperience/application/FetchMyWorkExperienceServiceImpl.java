package page.clab.api.domain.workExperience.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.workExperience.dao.WorkExperienceRepository;
import page.clab.api.domain.workExperience.domain.WorkExperience;
import page.clab.api.domain.workExperience.dto.response.WorkExperienceResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class FetchMyWorkExperienceServiceImpl implements FetchMyWorkExperienceService {

    private final MemberLookupService memberLookupService;
    private final WorkExperienceRepository workExperienceRepository;

    @Override
    @Transactional(readOnly = true)
    public PagedResponseDto<WorkExperienceResponseDto> fetchMyWorkExperience(Pageable pageable) {
        String currentMemberId = memberLookupService.getCurrentMemberId();
        Page<WorkExperience> workExperiences = workExperienceRepository.findByMemberId(currentMemberId, pageable);
        return new PagedResponseDto<>(workExperiences.map(WorkExperienceResponseDto::toDto));
    }
}
