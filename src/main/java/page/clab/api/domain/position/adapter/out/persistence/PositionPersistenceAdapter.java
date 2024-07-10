package page.clab.api.domain.position.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.position.application.port.out.RegisterPositionPort;
import page.clab.api.domain.position.application.port.out.RetrievePositionPort;
import page.clab.api.domain.position.application.port.out.UpdatePositionPort;
import page.clab.api.domain.position.domain.Position;
import page.clab.api.domain.position.domain.PositionType;
import page.clab.api.global.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PositionPersistenceAdapter implements
        RegisterPositionPort,
        RetrievePositionPort,
        UpdatePositionPort {

    private final PositionRepository repository;
    private final PositionMapper mapper;

    @Override
    public Position save(Position position) {
        PositionJpaEntity entity = mapper.toJpaEntity(position);
        PositionJpaEntity savedEntity = repository.save(entity);
        return mapper.toDomainEntity(savedEntity);
    }

    @Override
    public void saveAll(List<Position> positions) {
        List<PositionJpaEntity> entities = positions.stream()
                .map(mapper::toJpaEntity)
                .toList();
        repository.saveAll(entities);
    }

    @Override
    public Position update(Position position) {
        PositionJpaEntity entity = mapper.toJpaEntity(position);
        PositionJpaEntity updatedEntity = repository.save(entity);
        return mapper.toDomainEntity(updatedEntity);
    }

    @Override
    public Optional<Position> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomainEntity);
    }

    @Override
    public Position findByIdOrThrow(Long id) {
        return repository.findById(id)
                .map(mapper::toDomainEntity)
                .orElseThrow(() -> new NotFoundException("[Position] id: " + id + "에 해당하는 직책이 존재하지 않습니다."));
    }

    @Override
    public Optional<Position> findByMemberIdAndYearAndPositionType(String memberId, String year, PositionType positionType) {
        return repository.findByMemberIdAndYearAndPositionType(memberId, year, positionType)
                .map(mapper::toDomainEntity);
    }

    @Override
    public List<Position> findAllByMemberIdAndYearOrderByPositionTypeAsc(String memberId, String year) {
        return repository.findAllByMemberIdAndYearOrderByPositionTypeAsc(memberId, year).stream()
                .map(mapper::toDomainEntity)
                .toList();
    }

    @Override
    public Page<Position> findAllByIsDeletedTrue(Pageable pageable) {
        return repository.findAllByIsDeletedTrue(pageable)
                .map(mapper::toDomainEntity);
    }

    @Override
    public Page<Position> findByConditions(String year, PositionType positionType, Pageable pageable) {
        return repository.findByConditions(year, positionType, pageable)
                .map(mapper::toDomainEntity);
    }

    @Override
    public List<Position> findByMemberId(String memberId) {
        return repository.findByMemberId(memberId).stream()
                .map(mapper::toDomainEntity)
                .toList();
    }
}
