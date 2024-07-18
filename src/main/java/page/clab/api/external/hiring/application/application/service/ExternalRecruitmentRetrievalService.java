package page.clab.api.external.hiring.application.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.hiring.recruitment.application.port.out.RetrieveRecruitmentPort;
import page.clab.api.external.hiring.application.application.port.ExternalRetrieveRecruitmentUseCase;

@Service
@RequiredArgsConstructor
public class ExternalRecruitmentRetrievalService implements ExternalRetrieveRecruitmentUseCase {

    private final RetrieveRecruitmentPort retrieveRecruitmentPort;

    @Override
    public void ensureRecruitmentExists(Long recruitmentId) {
        retrieveRecruitmentPort.existsByIdOrThrow(recruitmentId);
    }
}
