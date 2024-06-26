package page.clab.api.domain.position.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.dto.shared.MemberPositionInfoDto;
import page.clab.api.domain.position.dao.PositionRepository;
import page.clab.api.domain.position.domain.Position;
import page.clab.api.domain.position.domain.PositionType;
import page.clab.api.domain.position.dto.request.PositionRequestDto;
import page.clab.api.domain.position.dto.response.PositionMyResponseDto;
import page.clab.api.domain.position.dto.response.PositionResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final MemberLookupService memberLookupService;

    private final PositionRepository positionRepository;

    @Transactional
    public Long createPosition(PositionRequestDto requestDto) {
        memberLookupService.ensureMemberExists(requestDto.getMemberId());
        return positionRepository.findByMemberIdAndYearAndPositionType(requestDto.getMemberId(), requestDto.getYear(), requestDto.getPositionType())
                .map(Position::getId)
                .orElseGet(() -> {
                    Position position = PositionRequestDto.toEntity(requestDto);
                    return positionRepository.save(position).getId();
                });
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<PositionResponseDto> getPositionsByConditions(String year, PositionType positionType, Pageable pageable) {
        MemberPositionInfoDto currentMemberInfo = memberLookupService.getCurrentMemberPositionInfo();
        Page<Position> positions = positionRepository.findByConditions(year, positionType, pageable);
        return new PagedResponseDto<>(positions.map(position -> PositionResponseDto.toDto(position, currentMemberInfo)));
    }

    @Transactional(readOnly = true)
    public PositionMyResponseDto getMyPositionsByYear(String year) {
        MemberPositionInfoDto currentMemberInfo = memberLookupService.getCurrentMemberPositionInfo();
        List<Position> positions = getPositionsByMemberIdAndYear(currentMemberInfo.getMemberId(), year);
        if (positions.isEmpty()) {
            throw new NotFoundException("해당 멤버의 " + year + "년도 직책이 존재하지 않습니다.");
        }
        return PositionMyResponseDto.toDto(positions, currentMemberInfo);
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<PositionResponseDto> getDeletedPositions(Pageable pageable) {
        MemberPositionInfoDto currentMemberInfo = memberLookupService.getCurrentMemberPositionInfo();
        Page<Position> positions = positionRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(positions.map(position -> PositionResponseDto.toDto(position, currentMemberInfo)));
    }

    @Transactional
    public Long deletePosition(Long positionId) {
        Position position = getPositionsByIdOrThrow(positionId);
        position.delete();
        return positionRepository.save(position).getId();
    }

    private Position getPositionsByIdOrThrow(Long positionId) {
        return positionRepository.findById(positionId)
                .orElseThrow(() -> new NotFoundException("해당 운영진이 존재하지 않습니다."));
    }

    private List<Position> getPositionsByMemberIdAndYear(String memberId, String year) {
        return positionRepository.findAllByMemberIdAndYearOrderByPositionTypeAsc(memberId, year);
    }

}
