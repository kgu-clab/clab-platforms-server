package page.clab.api.domain.position.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.position.application.DeletePositionService;
import page.clab.api.domain.position.dao.PositionRepository;
import page.clab.api.domain.position.domain.Position;
import page.clab.api.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class DeletePositionServiceImpl implements DeletePositionService {

    private final PositionRepository positionRepository;

    @Transactional
    public Long execute(Long positionId) {
        Position position = getPositionByIdOrThrow(positionId);
        position.delete();
        return positionRepository.save(position).getId();
    }

    private Position getPositionByIdOrThrow(Long positionId) {
        return positionRepository.findById(positionId)
                .orElseThrow(() -> new NotFoundException("해당 운영진이 존재하지 않습니다."));
    }
}
