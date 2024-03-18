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

@Service
@RequiredArgsConstructor
public class WorkExperienceService {

    private final MemberService memberService;

    private final WorkExperienceRepository workExperienceRepository;

    public Long createWorkExperience(WorkExperienceRequestDto workExperienceRequestDto) {
        Member member = memberService.getCurrentMember();
        WorkExperience workExperience = WorkExperience.of(workExperienceRequestDto, member);
        return workExperienceRepository.save(workExperience).getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<WorkExperienceResponseDto> getMyWorkExperience(Pageable pageable) {
        Member member = memberService.getCurrentMember();
        Page<WorkExperience> workExperiences = workExperienceRepository.findAllByMemberOrderByStartDateDesc(member, pageable);
        return new PagedResponseDto<>(workExperiences.map(WorkExperienceResponseDto::of));
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<WorkExperienceResponseDto> getWorkExperiencesByConditions(String memberId, Pageable pageable) {
        Member member = memberService.getMemberByIdOrThrow(memberId);
        Page<WorkExperience> workExperiences = workExperienceRepository.findAllByMemberOrderByStartDateDesc(member, pageable);
        return new PagedResponseDto<>(workExperiences.map(WorkExperienceResponseDto::of));
    }

    public Long updateWorkExperience(Long workExperienceId, WorkExperienceUpdateRequestDto workExperienceUpdateRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        WorkExperience workExperience = getWorkExperienceByIdOrThrow(workExperienceId);
        workExperience.validateAccessPermission(member);
        workExperience.update(workExperienceUpdateRequestDto);
        return workExperienceRepository.save(workExperience).getId();
    }

    public Long deleteWorkExperience(Long workExperienceId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        WorkExperience workExperience = getWorkExperienceByIdOrThrow(workExperienceId);
        workExperience.validateAccessPermission(member);
        workExperienceRepository.deleteById(workExperienceId);
        return workExperience.getId();
    }

    private WorkExperience getWorkExperienceByIdOrThrow(Long workExperienceId) {
        return workExperienceRepository.findById(workExperienceId)
                .orElseThrow(() -> new NotFoundException("해당 경력사항이 존재하지 않습니다."));
    }

}
