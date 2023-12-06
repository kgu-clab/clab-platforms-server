package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.repository.ExecutivesRepository;
import page.clab.api.type.dto.ExecutivesRequestDto;
import page.clab.api.type.dto.ExecutivesResponseDto;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.entity.Executives;
import page.clab.api.type.entity.Member;

@Service
@RequiredArgsConstructor
public class ExecutivesService {

    private final MemberService memberService;

    private final ExecutivesRepository executivesRepository;

    public Long createExecutives(ExecutivesRequestDto executivesRequestDto) {
        Member executivesMember = memberService.getMemberByIdOrThrow(executivesRequestDto.getMemberId());
        Executives executives = Executives.of(executivesRequestDto);
        return executivesRepository.save(executives).getId();
    }

    public PagedResponseDto<ExecutivesResponseDto> getExecutives(Pageable pageable) {
        Page<Executives> executives = executivesRepository.findAllByOrderByYearDescPositionAsc(pageable);
        return new PagedResponseDto<>(executives.map(ExecutivesResponseDto::of));
    }

    public PagedResponseDto<ExecutivesResponseDto> getExecutivesByYear(Pageable pageable, String year) {
        Page<Executives> executives = getExecutivesByYear(year, pageable);
        return new PagedResponseDto<>(executives.map(ExecutivesResponseDto::of));
    }

    public Long deleteExecutives(Long executivesId) {
        Executives executives = getExecutivesByIdOrThrow(executivesId);
        executivesRepository.delete(executives);
        return executives.getId();
    }

    private Executives getExecutivesByIdOrThrow(Long executivesId) {
        return executivesRepository.findById(executivesId)
                .orElseThrow(() -> new NotFoundException("해당 운영진이 존재하지 않습니다."));
    }

    private Page<Executives> getExecutivesByYear(String year, Pageable pageable) {
        return executivesRepository.findAllByYearOrderByPositionAsc(year, pageable);
    }

}
