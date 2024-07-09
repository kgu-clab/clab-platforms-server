package page.clab.api.domain.recruitment.application.port.in;

import page.clab.api.domain.recruitment.application.dto.request.RecruitmentRequestDto;

public interface RegisterRecruitmentUseCase {
    Long registerRecruitment(RecruitmentRequestDto requestDto);
}
