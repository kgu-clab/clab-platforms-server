package page.clab.api.domain.position.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.domain.member.dto.shared.MemberPositionInfoDto;
import page.clab.api.domain.position.application.MyPositionsByYearRetrievalService;
import page.clab.api.domain.position.dao.PositionRepository;
import page.clab.api.domain.position.domain.Position;
import page.clab.api.domain.position.dto.response.PositionMyResponseDto;
import page.clab.api.global.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPositionsByYearRetrievalServiceImpl implements MyPositionsByYearRetrievalService {

    private final MemberLookupService memberLookupService;
    private final PositionRepository positionRepository;

    @Transactional(readOnly = true)
    public PositionMyResponseDto retrieve(String year) {
        MemberPositionInfoDto currentMemberInfo = memberLookupService.getCurrentMemberPositionInfo();
        List<Position> positions = getPositionsByMemberIdAndYear(currentMemberInfo.getMemberId(), year);
        if (positions.isEmpty()) {
            throw new NotFoundException("해당 멤버의 " + year + "년도 직책이 존재하지 않습니다.");
        }
        return PositionMyResponseDto.toDto(positions, currentMemberInfo);
    }

    private List<Position> getPositionsByMemberIdAndYear(String memberId, String year) {
        return positionRepository.findAllByMemberIdAndYearOrderByPositionTypeAsc(memberId, year);
    }
}
