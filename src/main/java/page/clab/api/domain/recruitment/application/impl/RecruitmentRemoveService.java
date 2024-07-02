package page.clab.api.domain.recruitment.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.recruitment.application.RecruitmentLookupUseCase;
import page.clab.api.domain.recruitment.application.RecruitmentRemoveUseCase;
import page.clab.api.domain.recruitment.dao.RecruitmentRepository;
import page.clab.api.domain.recruitment.domain.Recruitment;

@Service
@RequiredArgsConstructor
public class RecruitmentRemoveService implements RecruitmentRemoveUseCase {

    private final RecruitmentLookupUseCase recruitmentLookupUseCase;

    private final RecruitmentRepository recruitmentRepository;

    @Transactional
    @Override
    public Long remove(Long recruitmentId) {
        Recruitment recruitment = recruitmentLookupUseCase.getRecruitmentByIdOrThrow(recruitmentId);
        recruitment.delete();
        return recruitmentRepository.save(recruitment).getId();
    }

}
