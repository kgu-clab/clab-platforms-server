package page.clab.api.domain.recruitment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.recruitment.application.port.in.RecruitmentRetrievalUseCase;
import page.clab.api.domain.recruitment.application.port.out.LoadRecruitmentPort;
import page.clab.api.domain.recruitment.domain.Recruitment;

@Service
@RequiredArgsConstructor
public class RecruitmentRetrievalService implements RecruitmentRetrievalUseCase {

    private final LoadRecruitmentPort loadRecruitmentPort;

    @Override
    public Recruitment findByIdOrThrow(Long recruitmentId) {
        return loadRecruitmentPort.findByIdOrThrow(recruitmentId);
    }
}
