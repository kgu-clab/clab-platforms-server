package page.clab.api.domain.position.dao;

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

    @Override
    public Position save(Position position) {
        return repository.save(position);
    }

    @Override
    public Position update(Position position) {
        return repository.save(position);
    }

    @Override
    public Optional<Position> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Position findByIdOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("[Position] id: " + id + "에 해당하는 직책이 존재하지 않습니다."));
    }

    @Override
    public Optional<Position> findByMemberIdAndYearAndPositionType(String memberId, String year, PositionType positionType) {
        return repository.findByMemberIdAndYearAndPositionType(memberId, year, positionType);
    }

    @Override
    public List<Position> findAllByMemberIdAndYearOrderByPositionTypeAsc(String memberId, String year) {
        return repository.findAllByMemberIdAndYearOrderByPositionTypeAsc(memberId, year);
    }

    @Override
    public Page<Position> findAllByIsDeletedTrue(Pageable pageable) {
        return repository.findAllByIsDeletedTrue(pageable);
    }

    @Override
    public Page<Position> findByConditions(String year, PositionType positionType, Pageable pageable) {
        return repository.findByConditions(year, positionType, pageable);
    }
}
