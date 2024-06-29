package page.clab.api.domain.recruitment.application;

import page.clab.api.domain.recruitment.dto.request.RecruitmentRequestDto;

public interface CreateRecruitmentService {
    Long execute(RecruitmentRequestDto requestDto);
}
