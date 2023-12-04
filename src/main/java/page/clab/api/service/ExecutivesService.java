package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.ExecutivesRepository;
import page.clab.api.type.dto.ExecutivesRequestDto;
import page.clab.api.type.dto.ExecutivesResponseDto;
import page.clab.api.type.dto.NotificationRequestDto;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.entity.Executives;
import page.clab.api.type.entity.Member;

@Service
@RequiredArgsConstructor
public class ExecutivesService {

    private final MemberService memberService;

    private final NotificationService notificationService;

    private final ExecutivesRepository executivesRepository;

    public void createExecutives(ExecutivesRequestDto executivesRequestDto) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        if (!memberService.isMemberAdminRole(member)) {
            throw new PermissionDeniedException("운영진을 등록할 권한이 없습니다.");
        }
        Member executivesMember = memberService.getMemberByIdOrThrow(executivesRequestDto.getMemberId());
        Executives executives = Executives.of(executivesRequestDto);
        executivesRepository.save(executives);

        NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                .memberId(executivesMember.getId())
                .content("새로운 운영진으로 등록되었습니다.")
                .build();
        notificationService.createNotification(notificationRequestDto);
    }

    public PagedResponseDto<ExecutivesResponseDto> getExecutives(Pageable pageable) {
        Page<Executives> executives = executivesRepository.findAllByOrderByYearDescPositionAsc(pageable);
        return new PagedResponseDto<>(executives.map(ExecutivesResponseDto::of));
    }

    public PagedResponseDto<ExecutivesResponseDto> getExecutivesByYear(Pageable pageable, String year) {
        Page<Executives> executives = getExecutivesByYear(year, pageable);
        return new PagedResponseDto<>(executives.map(ExecutivesResponseDto::of));
    }

    public void deleteExecutives(Long executivesId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        if (!memberService.isMemberAdminRole(member)) {
            throw new PermissionDeniedException("운영진을 목록에서 삭제할 권한이 없습니다.");
        }
        Executives executives = getExecutivesByIdOrThrow(executivesId);
        executivesRepository.delete(executives);
    }

    private Executives getExecutivesByIdOrThrow(Long executivesId) {
        return executivesRepository.findById(executivesId)
                .orElseThrow(() -> new NotFoundException("해당 운영진이 존재하지 않습니다."));
    }

    private Page<Executives> getExecutivesByYear(String year, Pageable pageable) {
        return executivesRepository.findAllByYearOrderByPositionAsc(year, pageable);
    }

}
