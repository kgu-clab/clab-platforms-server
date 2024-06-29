package page.clab.api.domain.workExperience.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.workExperience.application.WorkExperienceRemoveService;
import page.clab.api.domain.workExperience.dao.WorkExperienceRepository;
import page.clab.api.domain.workExperience.domain.WorkExperience;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;

@Service
@RequiredArgsConstructor
public class WorkExperienceRemoveServiceImpl implements WorkExperienceRemoveService {

    private final MemberLookupService memberLookupService;
    private final WorkExperienceRepository workExperienceRepository;

    @Override
    @Transactional
    public Long remove(Long workExperienceId) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = memberLookupService.getCurrentMemberDetailedInfo();
        WorkExperience workExperience = getWorkExperienceByIdOrThrow(workExperienceId);
        workExperience.validateAccessPermission(currentMemberInfo);
        workExperience.delete();
        return workExperienceRepository.save(workExperience).getId();
    }

    private WorkExperience getWorkExperienceByIdOrThrow(Long workExperienceId) {
        return workExperienceRepository.findById(workExperienceId)
                .orElseThrow(() -> new NotFoundException("해당 경력사항이 존재하지 않습니다."));
    }
}
