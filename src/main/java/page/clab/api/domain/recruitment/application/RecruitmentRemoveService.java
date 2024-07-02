package page.clab.api.domain.recruitment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.recruitment.application.port.in.RecruitmentLookupUseCase;
import page.clab.api.domain.recruitment.application.port.in.RecruitmentRemoveUseCase;
import page.clab.api.domain.recruitment.application.port.out.UpdateRecruitmentPort;
import page.clab.api.domain.recruitment.domain.Recruitment;

@Service
@RequiredArgsConstructor
public class RecruitmentRemoveService implements RecruitmentRemoveUseCase {

    private final RecruitmentLookupUseCase recruitmentLookupUseCase;
    private final UpdateRecruitmentPort updateRecruitmentPort;

    @Transactional
    @Override
    public Long remove(Long recruitmentId) {
        Recruitment recruitment = recruitmentLookupUseCase.getRecruitmentByIdOrThrow(recruitmentId);
        recruitment.delete();
        return updateRecruitmentPort.update(recruitment).getId();
    }
}
