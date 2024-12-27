package page.clab.api.domain.memberManagement.executive.adapter.out.persistence;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.domain.memberManagement.executive.application.port.out.RegisterExecutivePort;
import page.clab.api.domain.memberManagement.executive.application.port.out.RetrieveExecutivePort;
import page.clab.api.domain.memberManagement.executive.domain.Executive;

@Component
@RequiredArgsConstructor
public class ExecutivePersistenceAdapter implements
    RegisterExecutivePort,
    RetrieveExecutivePort {

    private final ExecutiveMapper executiveMapper;
    private final ExecutiveRepository executiveRepository;

    @Override
    public Executive save(Executive executive) {
        ExecutiveJpaEntity jpaEntity = executiveMapper.toEntity(executive);
        ExecutiveJpaEntity savedEntity = executiveRepository.save(jpaEntity);
        return executiveMapper.toDomain(savedEntity);
    }

    @Override
    public List<Executive> findAll() {
        List<ExecutiveJpaEntity> jpaEntities = executiveRepository.findAll();
        return jpaEntities.stream()
            .map(executiveMapper::toDomain)
            .collect(Collectors.toList());
    }
}
