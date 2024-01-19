package page.clab.api.domain.executive.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.executive.dao.ExecutiveRepository;
import page.clab.api.domain.executive.domain.Executive;
import page.clab.api.domain.executive.dto.request.ExecutivesRequestDto;
import page.clab.api.domain.executive.dto.response.ExecutivesResponseDto;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class ExecutiveService {

    private final MemberService memberService;

    private final ExecutiveRepository executiveRepository;

    public Long createExecutive(ExecutivesRequestDto executivesRequestDto) {
        Member executiveMember = memberService.getMemberByIdOrThrow(executivesRequestDto.getMemberId());
        Executive executive = Executive.of(executivesRequestDto);
        return executiveRepository.save(executive).getId();
    }

    public PagedResponseDto<ExecutivesResponseDto> getExecutives(Pageable pageable) {
        Page<Executive> executives = executiveRepository.findAllByOrderByYearDescPositionAsc(pageable);
        return new PagedResponseDto<>(executives.map(ExecutivesResponseDto::of));
    }

    public PagedResponseDto<ExecutivesResponseDto> getExecutivesByYear(Pageable pageable, String year) {
        Page<Executive> executives = getExecutivesByYear(year, pageable);
        return new PagedResponseDto<>(executives.map(ExecutivesResponseDto::of));
    }

    public Long deleteExecutive(Long executivesId) {
        Executive executive = getExecutivesByIdOrThrow(executivesId);
        executiveRepository.delete(executive);
        return executive.getId();
    }

    private Executive getExecutivesByIdOrThrow(Long executivesId) {
        return executiveRepository.findById(executivesId)
                .orElseThrow(() -> new NotFoundException("해당 운영진이 존재하지 않습니다."));
    }

    private Page<Executive> getExecutivesByYear(String year, Pageable pageable) {
        return executiveRepository.findAllByYearOrderByPositionAsc(year, pageable);
    }

}
