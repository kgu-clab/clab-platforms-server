package page.clab.api.domain.workExperience.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.workExperience.application.port.in.WorkExperienceRemoveUseCase;
import page.clab.api.domain.workExperience.application.port.out.RemoveWorkExperiencePort;
import page.clab.api.domain.workExperience.domain.WorkExperience;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class WorkExperienceRemoveService implements WorkExperienceRemoveUseCase {

    private final MemberLookupUseCase memberLookupUseCase;
    private final RemoveWorkExperiencePort removeWorkExperiencePort;

    @Override
    @Transactional
    public Long remove(Long workExperienceId) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = memberLookupUseCase.getCurrentMemberDetailedInfo();
        WorkExperience workExperience = removeWorkExperiencePort.findWorkExperienceByIdOrThrow(workExperienceId);
        workExperience.validateAccessPermission(currentMemberInfo);
        workExperience.delete();
        return removeWorkExperiencePort.save(workExperience).getId();
    }
}
