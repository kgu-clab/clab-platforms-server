package page.clab.api.domain.workExperience.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.workExperience.dao.WorkExperienceRepository;
import page.clab.api.domain.workExperience.domain.WorkExperience;
import page.clab.api.domain.workExperience.dto.request.WorkExperienceRequestDto;
import page.clab.api.domain.workExperience.dto.request.WorkExperienceUpdateRequestDto;
import page.clab.api.domain.workExperience.dto.response.WorkExperienceResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class WorkExperienceService {

    private final MemberLookupService memberLookupService;

    private final ValidationService validationService;

    private final WorkExperienceRepository workExperienceRepository;

    @Transactional
    public Long createWorkExperience(WorkExperienceRequestDto requestDto) {
        String currentMemberId = memberLookupService.getCurrentMemberId();
        WorkExperience workExperience = WorkExperienceRequestDto.toEntity(requestDto, currentMemberId);
        validationService.checkValid(workExperience);
        return workExperienceRepository.save(workExperience).getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<WorkExperienceResponseDto> getMyWorkExperience(Pageable pageable) {
        String currentMemberId = memberLookupService.getCurrentMemberId();
        Page<WorkExperience> workExperiences = workExperienceRepository.findByMemberId(currentMemberId, pageable);
        return new PagedResponseDto<>(workExperiences.map(WorkExperienceResponseDto::toDto));
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<WorkExperienceResponseDto> getWorkExperiencesByConditions(String memberId, Pageable pageable) {
        Page<WorkExperience> workExperiences = workExperienceRepository.findByMemberId(memberId, pageable);
        return new PagedResponseDto<>(workExperiences.map(WorkExperienceResponseDto::toDto));
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<WorkExperienceResponseDto> getDeletedWorkExperiences(Pageable pageable) {
        Page<WorkExperience> workExperiences = workExperienceRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(workExperiences.map(WorkExperienceResponseDto::toDto));
    }

    @Transactional
    public Long updateWorkExperience(Long workExperienceId, WorkExperienceUpdateRequestDto requestDto) throws PermissionDeniedException {
        MemberDetailedInfoDto currentMemberInfo = memberLookupService.getCurrentMemberDetailedInfo();
        WorkExperience workExperience = getWorkExperienceByIdOrThrow(workExperienceId);
        workExperience.validateAccessPermission(currentMemberInfo);
        workExperience.update(requestDto);
        validationService.checkValid(workExperience);
        return workExperienceRepository.save(workExperience).getId();
    }

    @Transactional
    public Long deleteWorkExperience(Long workExperienceId) throws PermissionDeniedException {
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
