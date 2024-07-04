package page.clab.api.domain.recruitment.application.port.in;

import page.clab.api.domain.recruitment.dto.request.RecruitmentUpdateRequestDto;

public interface UpdateRecruitmentUseCase {
    Long updateRecruitment(Long recruitmentId, RecruitmentUpdateRequestDto requestDto);
}
