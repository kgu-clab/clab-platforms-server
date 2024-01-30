package page.clab.api.domain.executive.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.executive.dao.ExecutiveRepository;
import page.clab.api.domain.executive.domain.Executive;
import page.clab.api.domain.executive.dto.request.ExecutiveRequestDto;
import page.clab.api.domain.executive.dto.response.ExecutiveResponseDto;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class ExecutiveService {

    private final MemberService memberService;

    private final ExecutiveRepository executiveRepository;

    public Long createExecutive(ExecutiveRequestDto executiveRequestDto) {
        Member executiveMember = memberService.getMemberByIdOrThrow(executiveRequestDto.getMemberId());
        Executive executive = Executive.of(executiveRequestDto);
        return executiveRepository.save(executive).getId();
    }

    public PagedResponseDto<ExecutiveResponseDto> getExecutives(Pageable pageable) {
        Page<Executive> executives = executiveRepository.findAllByOrderByYearDescPositionAsc(pageable);
        return new PagedResponseDto<>(executives.map(ExecutiveResponseDto::of));
    }

    public PagedResponseDto<ExecutiveResponseDto> getExecutivesByYear(Pageable pageable, String year) {
        Page<Executive> executives = getExecutivesByYear(year, pageable);
        return new PagedResponseDto<>(executives.map(ExecutiveResponseDto::of));
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
