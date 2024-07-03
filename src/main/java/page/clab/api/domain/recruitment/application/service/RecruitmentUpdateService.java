package page.clab.api.domain.recruitment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.recruitment.application.port.in.RetrieveRecruitmentUseCase;
import page.clab.api.domain.recruitment.application.port.in.UpdateRecruitmentUseCase;
import page.clab.api.domain.recruitment.application.port.out.UpdateRecruitmentPort;
import page.clab.api.domain.recruitment.domain.Recruitment;
import page.clab.api.domain.recruitment.dto.request.RecruitmentUpdateRequestDto;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class RecruitmentUpdateService implements UpdateRecruitmentUseCase {

    private final RetrieveRecruitmentUseCase retrieveRecruitmentUseCase;
    private final ValidationService validationService;
    private final UpdateRecruitmentPort updateRecruitmentPort;
    private final RecruitmentStatusUpdater recruitmentStatusUpdater;

    @Transactional
    @Override
    public Long update(Long recruitmentId, RecruitmentUpdateRequestDto requestDto) {
        Recruitment recruitment = retrieveRecruitmentUseCase.findByIdOrThrow(recruitmentId);
        recruitment.update(requestDto);
        recruitmentStatusUpdater.updateRecruitmentStatusByRecruitment(recruitment);
        validationService.checkValid(recruitment);
        return updateRecruitmentPort.update(recruitment).getId();
    }
}
