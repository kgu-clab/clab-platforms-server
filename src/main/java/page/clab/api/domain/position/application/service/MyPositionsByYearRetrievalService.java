package page.clab.api.domain.position.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.dto.shared.MemberPositionInfoDto;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.position.application.dto.response.PositionMyResponseDto;
import page.clab.api.domain.position.application.port.in.RetrieveMyPositionsByYearUseCase;
import page.clab.api.domain.position.application.port.out.RetrievePositionPort;
import page.clab.api.domain.position.domain.Position;
import page.clab.api.global.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPositionsByYearRetrievalService implements RetrieveMyPositionsByYearUseCase {

    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final RetrievePositionPort retrievePositionPort;

    @Transactional(readOnly = true)
    public PositionMyResponseDto retrieveMyPositionsByYear(String year) {
        MemberPositionInfoDto currentMemberInfo = retrieveMemberInfoUseCase.getCurrentMemberPositionInfo();
        List<Position> positions = retrievePositionPort.findAllByMemberIdAndYearOrderByPositionTypeAsc(
                currentMemberInfo.getMemberId(), year);
        if (positions.isEmpty()) {
            throw new NotFoundException("해당 멤버의 " + year + "년도 직책이 존재하지 않습니다.");
        }
        return PositionMyResponseDto.toDto(positions, currentMemberInfo);
    }
}
