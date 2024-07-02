package page.clab.api.domain.recruitment.application.port.in;

import page.clab.api.domain.recruitment.domain.Recruitment;

public interface RecruitmentLookupUseCase {
    Recruitment getRecruitmentByIdOrThrow(Long recruitmentId);
}
