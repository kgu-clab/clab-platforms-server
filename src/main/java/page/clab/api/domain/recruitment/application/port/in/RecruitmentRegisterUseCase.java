package page.clab.api.domain.recruitment.application.port.in;

import page.clab.api.domain.recruitment.dto.request.RecruitmentRequestDto;

public interface RecruitmentRegisterUseCase {
    Long register(RecruitmentRequestDto requestDto);
}
