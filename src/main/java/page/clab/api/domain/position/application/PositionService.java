package page.clab.api.domain.position.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.position.dao.PositionRepository;
import page.clab.api.domain.position.domain.Position;
import page.clab.api.domain.position.dto.request.PositionRequestDto;
import page.clab.api.domain.position.dto.response.PositionResponseDto;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final MemberService memberService;

    private final PositionRepository positionRepository;

    public Long createExecutive(PositionRequestDto positionRequestDto) {
        Member executiveMember = memberService.getMemberByIdOrThrow(positionRequestDto.getMemberId());
        Position position = Position.of(positionRequestDto);
        return positionRepository.save(position).getId();
    }

    public PagedResponseDto<PositionResponseDto> getExecutives(Pageable pageable) {
        Page<Position> executives = positionRepository.findAllByOrderByYearDescPositionAsc(pageable);
        return new PagedResponseDto<>(executives.map(PositionResponseDto::of));
    }

    public PagedResponseDto<PositionResponseDto> getExecutivesByYear(Pageable pageable, String year) {
        Page<Position> executives = getExecutivesByYear(year, pageable);
        return new PagedResponseDto<>(executives.map(PositionResponseDto::of));
    }

    public Long deleteExecutive(Long executivesId) {
        Position position = getExecutivesByIdOrThrow(executivesId);
        positionRepository.delete(position);
        return position.getId();
    }

    private Position getExecutivesByIdOrThrow(Long executivesId) {
        return positionRepository.findById(executivesId)
                .orElseThrow(() -> new NotFoundException("해당 운영진이 존재하지 않습니다."));
    }

    private Page<Position> getExecutivesByYear(String year, Pageable pageable) {
        return positionRepository.findAllByYearOrderByPositionAsc(year, pageable);
    }

}
