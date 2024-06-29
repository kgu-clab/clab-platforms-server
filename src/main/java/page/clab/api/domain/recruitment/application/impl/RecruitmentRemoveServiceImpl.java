package page.clab.api.domain.recruitment.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.recruitment.application.RecruitmentRemoveService;
import page.clab.api.domain.recruitment.application.RecruitmentLookupService;
import page.clab.api.domain.recruitment.dao.RecruitmentRepository;
import page.clab.api.domain.recruitment.domain.Recruitment;

@Service
@RequiredArgsConstructor
public class RecruitmentRemoveServiceImpl implements RecruitmentRemoveService {

    private final RecruitmentLookupService recruitmentLookupService;

    private final RecruitmentRepository recruitmentRepository;

    @Transactional
    @Override
    public Long remove(Long recruitmentId) {
        Recruitment recruitment = recruitmentLookupService.getRecruitmentByIdOrThrow(recruitmentId);
        recruitment.delete();
        return recruitmentRepository.save(recruitment).getId();
    }

}
