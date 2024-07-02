package page.clab.api.domain.accuse.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.accuse.application.port.out.RegisterAccusePort;
import page.clab.api.domain.accuse.application.port.out.RetrieveAccuseByMemberIdAndTargetPort;
import page.clab.api.domain.accuse.application.port.out.RetrieveAccuseByMemberIdPort;
import page.clab.api.domain.accuse.application.port.out.RetrieveAccuseByTargetPort;
import page.clab.api.domain.accuse.domain.Accuse;
import page.clab.api.domain.accuse.domain.TargetType;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccusePersistenceAdapter implements
        RegisterAccusePort,
        RetrieveAccuseByMemberIdAndTargetPort,
        RetrieveAccuseByTargetPort,
        RetrieveAccuseByMemberIdPort {

    private final AccuseRepository accuseRepository;

    @Override
    public Accuse save(Accuse accuse) {
        return accuseRepository.save(accuse);
    }

    @Override
    public Optional<Accuse> findByMemberIdAndTarget(String memberId, TargetType targetType, Long targetReferenceId) {
        return accuseRepository.findByMemberIdAndTarget(memberId, targetType, targetReferenceId);
    }

    @Override
    public List<Accuse> findByTargetOrderByCreatedAtDesc(TargetType targetType, Long targetReferenceId) {
        return accuseRepository.findByTargetOrderByCreatedAtDesc(targetType, targetReferenceId);
    }

    @Override
    public List<Accuse> findByTarget(TargetType targetType, Long targetReferenceId) {
        return accuseRepository.findByTarget(targetType, targetReferenceId);
    }

    @Override
    public Page<Accuse> findByMemberId(String memberId, Pageable pageable) {
        return accuseRepository.findByMemberId(memberId, pageable);
    }

    @Override
    public List<Accuse> findByMemberId(String memberId) {
        return accuseRepository.findByMemberId(memberId);
    }
}
