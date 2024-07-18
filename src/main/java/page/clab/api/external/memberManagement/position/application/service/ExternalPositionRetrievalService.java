package page.clab.api.external.memberManagement.position.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.memberManagement.position.application.port.out.RetrievePositionPort;
import page.clab.api.domain.memberManagement.position.domain.Position;
import page.clab.api.domain.memberManagement.position.domain.PositionType;
import page.clab.api.external.memberManagement.position.application.port.ExternalRetrievePositionUseCase;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExternalPositionRetrievalService implements ExternalRetrievePositionUseCase {

    private final RetrievePositionPort retrievePositionPort;

    @Override
    public Optional<Position> findByMemberIdAndYearAndPositionType(String memberId, String year, PositionType positionType) {
        return retrievePositionPort.findByMemberIdAndYearAndPositionType(memberId, year, positionType);
    }
}
