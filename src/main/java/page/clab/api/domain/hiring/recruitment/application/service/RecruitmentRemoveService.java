package page.clab.api.domain.hiring.recruitment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.hiring.recruitment.application.port.in.RemoveRecruitmentUseCase;
import page.clab.api.domain.hiring.recruitment.application.port.in.RetrieveRecruitmentUseCase;
import page.clab.api.domain.hiring.recruitment.application.port.out.UpdateRecruitmentPort;
import page.clab.api.domain.hiring.recruitment.domain.Recruitment;

@Service
@RequiredArgsConstructor
public class RecruitmentRemoveService implements RemoveRecruitmentUseCase {

    private final RetrieveRecruitmentUseCase retrieveRecruitmentUseCase;
    private final UpdateRecruitmentPort updateRecruitmentPort;

    @Transactional
    @Override
    public Long removeRecruitment(Long recruitmentId) {
        Recruitment recruitment = retrieveRecruitmentUseCase.findByIdOrThrow(recruitmentId);
        recruitment.delete();
        return updateRecruitmentPort.update(recruitment).getId();
    }
}
