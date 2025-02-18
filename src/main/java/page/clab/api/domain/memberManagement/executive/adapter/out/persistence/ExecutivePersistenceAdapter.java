package page.clab.api.domain.memberManagement.executive.adapter.out.persistence;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.domain.memberManagement.executive.application.port.out.RegisterExecutivePort;
import page.clab.api.domain.memberManagement.executive.application.port.out.RetrieveExecutivePort;
import page.clab.api.domain.memberManagement.executive.application.port.out.UpdateExecutivePort;
import page.clab.api.domain.memberManagement.executive.domain.Executive;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;

@Component
@RequiredArgsConstructor
public class ExecutivePersistenceAdapter implements
    RegisterExecutivePort,
    RetrieveExecutivePort,
    UpdateExecutivePort {

    private final ExecutiveMapper executiveMapper;
    private final ExecutiveRepository executiveRepository;

    @Override
    public Executive save(Executive executive) {
        ExecutiveJpaEntity jpaEntity = executiveMapper.toEntity(executive);
        ExecutiveJpaEntity savedEntity = executiveRepository.save(jpaEntity);
        return executiveMapper.toDomain(savedEntity);
    }

    @Override
    public Executive update(Executive executive) {
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

    @Override
    public Executive getById(String executiveId) {
        ExecutiveJpaEntity jpaEntity = executiveRepository.findById(executiveId)
            .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND,
                "[Executive] id: " + executiveId + "에 해당하는 운영진이 존재하지 않습니다."));
        return executiveMapper.toDomain(jpaEntity);
    }

    @Override
    public Boolean existsById(String executiveId) {
        return executiveRepository.existsById(executiveId);
    }
}
