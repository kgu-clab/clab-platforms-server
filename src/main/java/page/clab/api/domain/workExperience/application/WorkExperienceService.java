package page.clab.api.domain.workExperience.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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

    public Long updateWorkExperience(Long workExperienceId, WorkExperienceUpdateRequestDto workExperienceUpdateRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        WorkExperience workExperience = getWorkExperienceByIdOrThrow(workExperienceId);
        if (!(workExperience.getMember().getId().equals(member.getId()) || memberService.isMemberSuperRole(member))) {
            throw new PermissionDeniedException("해당 경력사항을 수정할 권한이 없습니다.");
        }
        workExperience.update(workExperienceUpdateRequestDto);
        return workExperienceRepository.save(workExperience).getId();
    }

    public Long deleteWorkExperience(Long workExperienceId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        WorkExperience workExperience = getWorkExperienceByIdOrThrow(workExperienceId);
        if (!(workExperience.getMember().getId().equals(member.getId()) || memberService.isMemberSuperRole(member))) {
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
