package page.clab.api.domain.accuse.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.accuse.application.port.out.RegisterAccuseTargetPort;
import page.clab.api.domain.accuse.application.port.out.RetrieveAccuseTargetPort;
import page.clab.api.domain.accuse.domain.AccuseStatus;
import page.clab.api.domain.accuse.domain.AccuseTarget;
import page.clab.api.domain.accuse.domain.AccuseTargetId;
import page.clab.api.domain.accuse.domain.TargetType;
import page.clab.api.global.exception.NotFoundException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccuseTargetPersistenceAdapter implements
        RegisterAccuseTargetPort,
        RetrieveAccuseTargetPort {

    private final AccuseTargetRepository accuseTargetRepository;

    @Override
    public AccuseTarget save(AccuseTarget accuseTarget) {
        return accuseTargetRepository.save(accuseTarget);
    }

    @Override
    public Optional<AccuseTarget> findById(AccuseTargetId accuseTargetId) {
        return accuseTargetRepository.findById(accuseTargetId);
    }

    @Override
    public AccuseTarget findByIdOrThrow(AccuseTargetId accuseTargetId) {
        return accuseTargetRepository.findById(accuseTargetId)
                .orElseThrow(() -> new NotFoundException("[AccuseTarget] id: " + accuseTargetId + "에 해당하는 신고 대상이 존재하지 않습니다."));
    }

    @Override
    public Page<AccuseTarget> findByConditions(TargetType type, AccuseStatus status, boolean countOrder, Pageable pageable) {
        return accuseTargetRepository.findByConditions(type, status, countOrder, pageable);
    }
}
