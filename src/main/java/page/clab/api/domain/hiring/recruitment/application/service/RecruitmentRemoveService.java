package page.clab.api.domain.hiring.recruitment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.hiring.recruitment.application.port.in.RemoveRecruitmentUseCase;
import page.clab.api.domain.hiring.recruitment.application.port.out.RegisterRecruitmentPort;
import page.clab.api.domain.hiring.recruitment.application.port.out.RetrieveRecruitmentPort;
import page.clab.api.domain.hiring.recruitment.domain.Recruitment;

@Service
@RequiredArgsConstructor
public class RecruitmentRemoveService implements RemoveRecruitmentUseCase {

    private final RetrieveRecruitmentPort retrieveRecruitmentPort;
    private final RegisterRecruitmentPort registerRecruitmentPort;

    @Transactional
    @Override
    public Long removeRecruitment(Long recruitmentId) {
        Recruitment recruitment = retrieveRecruitmentPort.getById(recruitmentId);
        recruitment.delete();
        return registerRecruitmentPort.save(recruitment).getId();
    }
}
