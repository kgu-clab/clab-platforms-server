package page.clab.api.domain.memberManagement.workExperience.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.memberManagement.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.memberManagement.workExperience.application.dto.request.WorkExperienceUpdateRequestDto;
import page.clab.api.domain.memberManagement.workExperience.application.port.in.UpdateWorkExperienceUseCase;
import page.clab.api.domain.memberManagement.workExperience.application.port.out.RetrieveWorkExperiencePort;
import page.clab.api.domain.memberManagement.workExperience.application.port.out.UpdateWorkExperiencePort;
import page.clab.api.domain.memberManagement.workExperience.domain.WorkExperience;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class WorkExperienceUpdateService implements UpdateWorkExperienceUseCase {

    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final RetrieveWorkExperiencePort retrieveWorkExperiencePort;
    private final UpdateWorkExperiencePort updateWorkExperiencePort;

    @Override
    @Transactional
    public Long updateWorkExperience(Long workExperienceId, WorkExperienceUpdateRequestDto requestDto) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = retrieveMemberInfoUseCase.getCurrentMemberDetailedInfo();
        WorkExperience workExperience = retrieveWorkExperiencePort.findByIdOrThrow(workExperienceId);
        workExperience.validateAccessPermission(currentMemberInfo);
        workExperience.update(requestDto);
        workExperience.validateBusinessRules();
        return updateWorkExperiencePort.update(workExperience).getId();
    }
}
