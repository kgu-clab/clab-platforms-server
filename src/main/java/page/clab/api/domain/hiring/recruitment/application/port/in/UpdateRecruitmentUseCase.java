package page.clab.api.domain.hiring.recruitment.application.port.in;

import page.clab.api.domain.hiring.recruitment.application.dto.request.RecruitmentUpdateRequestDto;

public interface UpdateRecruitmentUseCase {

    Long updateRecruitment(Long recruitmentId, RecruitmentUpdateRequestDto requestDto);
}
