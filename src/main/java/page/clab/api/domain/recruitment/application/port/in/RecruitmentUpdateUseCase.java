package page.clab.api.domain.recruitment.application.port.in;

import page.clab.api.domain.recruitment.dto.request.RecruitmentUpdateRequestDto;

public interface RecruitmentUpdateUseCase {
    Long update(Long recruitmentId, RecruitmentUpdateRequestDto requestDto);
}
