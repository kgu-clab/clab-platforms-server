package page.clab.api.domain.position.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.port.in.EnsureMemberExistenceUseCase;
import page.clab.api.domain.position.application.port.in.RegisterPositionUseCase;
import page.clab.api.domain.position.application.port.out.RegisterPositionPort;
import page.clab.api.domain.position.application.port.out.RetrievePositionByMemberIdAndYearAndPositionTypePort;
import page.clab.api.domain.position.domain.Position;
import page.clab.api.domain.position.dto.request.PositionRequestDto;

@Service
@RequiredArgsConstructor
public class PositionRegisterService implements RegisterPositionUseCase {

    private final EnsureMemberExistenceUseCase ensureMemberExistenceUseCase;
    private final RegisterPositionPort registerPositionPort;
    private final RetrievePositionByMemberIdAndYearAndPositionTypePort retrievePositionByMemberIdAndYearAndPositionTypePort;

    @Transactional
    public Long register(PositionRequestDto requestDto) {
        ensureMemberExistenceUseCase.ensureMemberExists(requestDto.getMemberId());
        return retrievePositionByMemberIdAndYearAndPositionTypePort.findByMemberIdAndYearAndPositionType(
                        requestDto.getMemberId(), requestDto.getYear(), requestDto.getPositionType())
                .map(Position::getId)
                .orElseGet(() -> {
                    Position position = PositionRequestDto.toEntity(requestDto);
                    return registerPositionPort.save(position).getId();
                });
    }
}
