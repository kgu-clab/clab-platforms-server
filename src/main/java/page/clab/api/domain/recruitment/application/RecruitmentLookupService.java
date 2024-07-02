package page.clab.api.domain.recruitment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.recruitment.application.port.in.RecruitmentLookupUseCase;
import page.clab.api.domain.recruitment.application.port.out.LoadRecruitmentPort;
import page.clab.api.domain.recruitment.domain.Recruitment;

@Service
@RequiredArgsConstructor
public class RecruitmentLookupService implements RecruitmentLookupUseCase {

    private final LoadRecruitmentPort loadRecruitmentPort;

    @Override
    public Recruitment getRecruitmentByIdOrThrow(Long recruitmentId) {
        return loadRecruitmentPort.findByIdOrThrow(recruitmentId);
    }
}
