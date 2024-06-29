package page.clab.api.domain.recruitment.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.recruitment.application.DeleteRecruitmentService;
import page.clab.api.domain.recruitment.application.RecruitmentLookupService;
import page.clab.api.domain.recruitment.dao.RecruitmentRepository;
import page.clab.api.domain.recruitment.domain.Recruitment;

@Service
@RequiredArgsConstructor
public class DeleteRecruitmentServiceImpl implements DeleteRecruitmentService {

    private final RecruitmentLookupService recruitmentLookupService;

    private final RecruitmentRepository recruitmentRepository;

    @Transactional
    @Override
    public Long execute(Long recruitmentId) {
        Recruitment recruitment = recruitmentLookupService.getRecruitmentByIdOrThrow(recruitmentId);
        recruitment.delete();
        return recruitmentRepository.save(recruitment).getId();
    }

}
