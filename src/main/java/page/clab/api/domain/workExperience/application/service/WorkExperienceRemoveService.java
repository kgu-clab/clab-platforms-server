package page.clab.api.domain.workExperience.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.workExperience.application.port.in.RemoveWorkExperienceUseCase;
import page.clab.api.domain.workExperience.application.port.out.LoadWorkExperiencePort;
import page.clab.api.domain.workExperience.application.port.out.RegisterWorkExperiencePort;
import page.clab.api.domain.workExperience.domain.WorkExperience;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class WorkExperienceRemoveService implements RemoveWorkExperienceUseCase {

    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final LoadWorkExperiencePort loadWorkExperiencePort;
    private final RegisterWorkExperiencePort registerWorkExperiencePort;

    @Override
    @Transactional
    public Long remove(Long workExperienceId) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = retrieveMemberInfoUseCase.getCurrentMemberDetailedInfo();
        WorkExperience workExperience = loadWorkExperiencePort.findByIdOrThrow(workExperienceId);
        workExperience.validateAccessPermission(currentMemberInfo);
        workExperience.delete();
        return registerWorkExperiencePort.save(workExperience).getId();
    }
}
