package page.clab.api.domain.position.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.position.dao.PositionRepository;
import page.clab.api.domain.position.domain.Position;
import page.clab.api.domain.position.domain.PositionType;
import page.clab.api.domain.position.dto.request.PositionRequestDto;
import page.clab.api.domain.position.dto.response.PositionMyResponseDto;
import page.clab.api.domain.position.dto.response.PositionResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final MemberService memberService;

    private final PositionRepository positionRepository;

    public Long createPosition(PositionRequestDto positionRequestDto) {
        Member member = memberService.getMemberByIdOrThrow(positionRequestDto.getMemberId());
        Position position = getPositionByMemberAndYearAndPositionType(positionRequestDto, member);
        if (position == null) {
            position = Position.of(positionRequestDto);
            return positionRepository.save(position).getId();
        }
        return position.getId();
    }

    public PagedResponseDto<PositionResponseDto> searchPositions(String year, PositionType positionType, Pageable pageable) {
        Page<Position> positions = null;
        if (year != null && positionType != null) {
            positions = getPositionsByYearAndPositionTypeOrderByPositionTypeAsc(year, positionType, pageable);
        } else if (year != null) {
            positions = getPositionsByYearOrderByPositionTypeAsc(year, pageable);
        } else if (positionType != null) {
            positions = getPositionsByPositionTypeOrderByYearDescPositionTypeAsc(positionType, pageable);
        } else {
            positions = getPositionsByOrderByYearDescPositionTypeAsc(pageable);
        }
        return new PagedResponseDto<>(positions.map(PositionResponseDto::of));
    }

    public PositionMyResponseDto getMyPositionsByYear(String year) {
        Member member = memberService.getCurrentMember();
        List<Position> positions = getPositionsByMemberAndYear(member, year);
        return PositionMyResponseDto.of(positions);
    }

    public Long deletePosition(Long positionId) {
        Position position = getPositionsByIdOrThrow(positionId);
        positionRepository.delete(position);
        return position.getId();
    }

    private Position getPositionsByIdOrThrow(Long positionId) {
        return positionRepository.findById(positionId)
                .orElseThrow(() -> new NotFoundException("해당 운영진이 존재하지 않습니다."));
    }

    private Page<Position> getPositionsByYearAndPositionTypeOrderByPositionTypeAsc(String year, PositionType positionType, Pageable pageable) {
        return positionRepository.findAllByYearAndPositionTypeOrderByPositionTypeAsc(year, positionType, pageable);
    }

    private Page<Position> getPositionsByYearOrderByPositionTypeAsc(String year, Pageable pageable) {
        return positionRepository.findAllByYearOrderByPositionTypeAsc(year, pageable);
    }

    private Page<Position> getPositionsByPositionTypeOrderByYearDescPositionTypeAsc(PositionType positionType, Pageable pageable) {
        return positionRepository.findAllByPositionTypeOrderByYearDescPositionTypeAsc(positionType, pageable);
    }

    private Page<Position> getPositionsByOrderByYearDescPositionTypeAsc(Pageable pageable) {
        return positionRepository.findAllByOrderByYearDescPositionTypeAsc(pageable);
    }

    private Position getPositionByMemberAndYearAndPositionType(PositionRequestDto positionRequestDto, Member member) {
        return positionRepository.findByMemberAndYearAndPositionType(member, positionRequestDto.getYear(), positionRequestDto.getPositionType()).orElse(null);
    }

    private List<Position> getPositionsByMemberAndYear(Member member, String year) {
        return positionRepository.findAllByMemberAndYearOrderByPositionTypeAsc(member, year);
    }

}
