package page.clab.api.domain.hiring.recruitment.application.port.in;

import page.clab.api.domain.hiring.recruitment.application.dto.request.RecruitmentRequestDto;

public interface RegisterRecruitmentUseCase {

    Long registerRecruitment(RecruitmentRequestDto requestDto);
}
