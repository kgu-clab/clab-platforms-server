package page.clab.api.domain.recruitment.application;

import page.clab.api.domain.recruitment.dto.request.RecruitmentRequestDto;

public interface RecruitmentRegisterService {
    Long register(RecruitmentRequestDto requestDto);
}
