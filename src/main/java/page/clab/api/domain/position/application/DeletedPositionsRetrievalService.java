package page.clab.api.domain.position.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.MemberInfoRetrievalUseCase;
import page.clab.api.domain.member.dto.shared.MemberPositionInfoDto;
import page.clab.api.domain.position.application.port.in.DeletedPositionsRetrievalUseCase;
import page.clab.api.domain.position.application.port.out.RetrieveDeletedPositionsPort;
import page.clab.api.domain.position.domain.Position;
import page.clab.api.domain.position.dto.response.PositionResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;

@Service
@RequiredArgsConstructor
public class DeletedPositionsRetrievalService implements DeletedPositionsRetrievalUseCase {

    private final RetrieveDeletedPositionsPort retrieveDeletedPositionsPort;
    private final MemberInfoRetrievalUseCase memberInfoRetrievalUseCase;

    @Transactional(readOnly = true)
    public PagedResponseDto<PositionResponseDto> retrieve(Pageable pageable) {
        MemberPositionInfoDto currentMemberInfo = memberInfoRetrievalUseCase.getCurrentMemberPositionInfo();
        Page<Position> positions = retrieveDeletedPositionsPort.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(positions.map(position -> PositionResponseDto.toDto(position, currentMemberInfo)));
    }
}
