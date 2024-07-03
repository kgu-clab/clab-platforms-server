package page.clab.api.domain.workExperience.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.MemberInfoRetrievalUseCase;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.workExperience.application.port.in.WorkExperienceUpdateUseCase;
import page.clab.api.domain.workExperience.application.port.out.LoadWorkExperiencePort;
import page.clab.api.domain.workExperience.application.port.out.UpdateWorkExperiencePort;
import page.clab.api.domain.workExperience.domain.WorkExperience;
import page.clab.api.domain.workExperience.dto.request.WorkExperienceUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class WorkExperienceUpdateService implements WorkExperienceUpdateUseCase {

    private final MemberInfoRetrievalUseCase memberInfoRetrievalUseCase;
    private final ValidationService validationService;
    private final LoadWorkExperiencePort loadWorkExperiencePort;
    private final UpdateWorkExperiencePort updateWorkExperiencePort;

    @Override
    @Transactional
    public Long update(Long workExperienceId, WorkExperienceUpdateRequestDto requestDto) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = memberInfoRetrievalUseCase.getCurrentMemberDetailedInfo();
        WorkExperience workExperience = loadWorkExperiencePort.findByIdOrThrow(workExperienceId);
        workExperience.validateAccessPermission(currentMemberInfo);
        workExperience.update(requestDto);
        validationService.checkValid(workExperience);
        return updateWorkExperiencePort.update(workExperience).getId();
    }
}
