package page.clab.api.domain.position.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.MemberLookupUseCase;
import page.clab.api.domain.member.dto.shared.MemberPositionInfoDto;
import page.clab.api.domain.position.application.port.in.MyPositionsByYearRetrievalUseCase;
import page.clab.api.domain.position.application.port.out.RetrieveAllPositionsByMemberIdAndYearPort;
import page.clab.api.domain.position.domain.Position;
import page.clab.api.domain.position.dto.response.PositionMyResponseDto;
import page.clab.api.global.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPositionsByYearRetrievalService implements MyPositionsByYearRetrievalUseCase {

    private final MemberLookupUseCase memberLookupUseCase;
    private final RetrieveAllPositionsByMemberIdAndYearPort retrieveAllPositionsByMemberIdAndYearPort;

    @Transactional(readOnly = true)
    public PositionMyResponseDto retrieve(String year) {
        MemberPositionInfoDto currentMemberInfo = memberLookupUseCase.getCurrentMemberPositionInfo();
        List<Position> positions = retrieveAllPositionsByMemberIdAndYearPort.findAllByMemberIdAndYearOrderByPositionTypeAsc(
                currentMemberInfo.getMemberId(), year);
        if (positions.isEmpty()) {
            throw new NotFoundException("해당 멤버의 " + year + "년도 직책이 존재하지 않습니다.");
        }
        return PositionMyResponseDto.toDto(positions, currentMemberInfo);
    }
}
