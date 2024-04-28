package page.clab.api.domain.workExperience.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
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

    private final MemberService memberService;

    private final ValidationService validationService;

    private final WorkExperienceRepository workExperienceRepository;

    @Transactional
    public Long createWorkExperience(WorkExperienceRequestDto requestDto) {
        Member currentMember = memberService.getCurrentMember();
        WorkExperience workExperience = WorkExperienceRequestDto.toEntity(requestDto, currentMember);
        validationService.checkValid(workExperience);
        return workExperienceRepository.save(workExperience).getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<WorkExperienceResponseDto> getMyWorkExperience(Pageable pageable) {
        Member currentMember = memberService.getCurrentMember();
        Page<WorkExperience> workExperiences = workExperienceRepository.findAllByMemberOrderByStartDateDesc(currentMember, pageable);
        return new PagedResponseDto<>(workExperiences.map(WorkExperienceResponseDto::toDto));
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<WorkExperienceResponseDto> getWorkExperiencesByConditions(String memberId, Pageable pageable) {
        Member member = memberService.getMemberByIdOrThrow(memberId);
        Page<WorkExperience> workExperiences = workExperienceRepository.findAllByMemberOrderByStartDateDesc(member, pageable);
        return new PagedResponseDto<>(workExperiences.map(WorkExperienceResponseDto::toDto));
    }

    public PagedResponseDto<WorkExperienceResponseDto> getDeletedWorkExperiences(Pageable pageable) {
        Page<WorkExperience> workExperiences = workExperienceRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(workExperiences.map(WorkExperienceResponseDto::toDto));
    }

    @Transactional
    public Long updateWorkExperience(Long workExperienceId, WorkExperienceUpdateRequestDto requestDto) throws PermissionDeniedException {
        Member currentMember = memberService.getCurrentMember();
        WorkExperience workExperience = getWorkExperienceByIdOrThrow(workExperienceId);
        workExperience.validateAccessPermission(currentMember);
        workExperience.update(requestDto);
        validationService.checkValid(workExperience);
        return workExperienceRepository.save(workExperience).getId();
    }

    public Long deleteWorkExperience(Long workExperienceId) throws PermissionDeniedException {
        Member currentMember = memberService.getCurrentMember();
        WorkExperience workExperience = getWorkExperienceByIdOrThrow(workExperienceId);
        workExperience.validateAccessPermission(currentMember);
        workExperienceRepository.deleteById(workExperienceId);
        return workExperience.getId();
    }

    private WorkExperience getWorkExperienceByIdOrThrow(Long workExperienceId) {
        return workExperienceRepository.findById(workExperienceId)
                .orElseThrow(() -> new NotFoundException("해당 경력사항이 존재하지 않습니다."));
    }

}
