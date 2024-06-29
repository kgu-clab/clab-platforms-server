package page.clab.api.domain.recruitment.application;

import page.clab.api.domain.recruitment.dto.request.RecruitmentUpdateRequestDto;

public interface RecruitmentUpdateService {
    Long update(Long recruitmentId, RecruitmentUpdateRequestDto requestDto);
}
