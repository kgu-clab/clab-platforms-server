package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.WorkExperienceRepository;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.dto.WorkExperienceRequestDto;
import page.clab.api.type.dto.WorkExperienceResponseDto;
import page.clab.api.type.entity.Member;
import page.clab.api.type.entity.WorkExperience;

@Service
@RequiredArgsConstructor
public class WorkExperienceService {

    private final MemberService memberService;

    private final WorkExperienceRepository workExperienceRepository;

    public Long createWorkExperience(WorkExperienceRequestDto workExperienceRequestDto) {
        Member member = memberService.getCurrentMember();
        WorkExperience workExperience = WorkExperience.of(workExperienceRequestDto);
        workExperience.setMember(member);
        return workExperienceRepository.save(workExperience).getId();
    }

    public PagedResponseDto<WorkExperienceResponseDto> getMyWorkExperience(Pageable pageable) {
        Member member = memberService.getCurrentMember();
        Page<WorkExperience> workExperiences = workExperienceRepository.findAllByMember_IdOrderByStartDateDesc(member.getId(), pageable);
        return new PagedResponseDto<>(workExperiences.map(WorkExperienceResponseDto::of));
    }

    public PagedResponseDto<WorkExperienceResponseDto> searchWorkExperience(String memberId, Pageable pageable) {
        Member member = memberService.getMemberByIdOrThrow(memberId);
        Page<WorkExperience> workExperiences = workExperienceRepository.findAllByMember_IdOrderByStartDateDesc(member.getId(), pageable);
        return new PagedResponseDto<>(workExperiences.map(WorkExperienceResponseDto::of));
    }

    public Long updateWorkExperience(Long workExperienceId, WorkExperienceRequestDto workExperienceRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        WorkExperience workExperience = getWorkExperienceByIdOrThrow(workExperienceId);
        if (!(workExperience.getMember().getId().equals(member.getId()) || memberService.isMemberAdminRole(member))) {
            throw new PermissionDeniedException("해당 경력사항을 수정할 권한이 없습니다.");
        }
        WorkExperience updatedWorkExperience = WorkExperience.of(workExperienceRequestDto);
        updatedWorkExperience.setId(workExperienceId);
        updatedWorkExperience.setMember(member);
        return workExperienceRepository.save(updatedWorkExperience).getId();
    }

    public Long deleteWorkExperience(Long workExperienceId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        WorkExperience workExperience = getWorkExperienceByIdOrThrow(workExperienceId);
        if (!(workExperience.getMember().getId().equals(member.getId()) || memberService.isMemberAdminRole(member))) {
            throw new PermissionDeniedException("해당 경력사항을 삭제할 권한이 없습니다.");
        }
        workExperienceRepository.deleteById(workExperienceId);
        return workExperience.getId();
    }

    private WorkExperience getWorkExperienceByIdOrThrow(Long workExperienceId) {
    return workExperienceRepository.findById(workExperienceId)
            .orElseThrow(() -> new NotFoundException("해당 경력사항이 존재하지 않습니다."));
    }

}
