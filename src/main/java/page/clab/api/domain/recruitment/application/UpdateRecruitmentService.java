package page.clab.api.domain.recruitment.application;

import page.clab.api.domain.recruitment.dto.request.RecruitmentUpdateRequestDto;

public interface UpdateRecruitmentService {
    Long execute(Long recruitmentId, RecruitmentUpdateRequestDto requestDto);
}
