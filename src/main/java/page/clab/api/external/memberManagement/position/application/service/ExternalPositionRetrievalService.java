package page.clab.api.external.memberManagement.position.application.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.memberManagement.position.application.port.out.RetrievePositionPort;
import page.clab.api.domain.memberManagement.position.domain.Position;
import page.clab.api.domain.memberManagement.position.domain.PositionType;
import page.clab.api.external.memberManagement.position.application.port.ExternalRetrievePositionUseCase;

@Service
@RequiredArgsConstructor
public class ExternalPositionRetrievalService implements ExternalRetrievePositionUseCase {

    private final RetrievePositionPort retrievePositionPort;

    @Transactional(readOnly = true)
    @Override
    public Optional<Position> findByMemberIdAndYearAndPositionType(String memberId, String year,
        PositionType positionType) {
        return retrievePositionPort.findByMemberIdAndYearAndPositionType(memberId, year, positionType);
    }
}
