package page.clab.api.domain.recruitment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.recruitment.application.port.in.RetrieveRecruitmentUseCase;
import page.clab.api.domain.recruitment.application.port.out.RetrieveRecruitmentPort;
import page.clab.api.domain.recruitment.domain.Recruitment;

@Service
@RequiredArgsConstructor
public class RecruitmentRetrievalService implements RetrieveRecruitmentUseCase {

    private final RetrieveRecruitmentPort retrieveRecruitmentPort;

    @Override
    public Recruitment findByIdOrThrow(Long recruitmentId) {
        return retrieveRecruitmentPort.findByIdOrThrow(recruitmentId);
    }
}
