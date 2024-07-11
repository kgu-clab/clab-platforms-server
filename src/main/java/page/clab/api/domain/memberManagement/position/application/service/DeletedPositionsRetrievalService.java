package page.clab.api.domain.memberManagement.position.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberPositionInfoDto;
import page.clab.api.domain.memberManagement.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.memberManagement.position.application.dto.response.PositionResponseDto;
import page.clab.api.domain.memberManagement.position.application.port.in.RetrieveDeletedPositionsUseCase;
import page.clab.api.domain.memberManagement.position.application.port.out.RetrievePositionPort;
import page.clab.api.domain.memberManagement.position.domain.Position;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedPositionsRetrievalService implements RetrieveDeletedPositionsUseCase {

    private final RetrievePositionPort retrievePositionPort;
    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;

    @Transactional(readOnly = true)
    public PagedResponseDto<PositionResponseDto> retrieveDeletedPositions(Pageable pageable) {
        MemberPositionInfoDto currentMemberInfo = retrieveMemberInfoUseCase.getCurrentMemberPositionInfo();
        Page<Position> positions = retrievePositionPort.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(positions.map(position -> PositionResponseDto.toDto(position, currentMemberInfo)));
    }
}
