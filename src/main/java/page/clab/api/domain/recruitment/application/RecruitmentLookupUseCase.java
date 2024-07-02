package page.clab.api.domain.recruitment.application;

import page.clab.api.domain.recruitment.domain.Recruitment;

public interface RecruitmentLookupUseCase {
    Recruitment getRecruitmentByIdOrThrow(Long recruitmentId);
}
