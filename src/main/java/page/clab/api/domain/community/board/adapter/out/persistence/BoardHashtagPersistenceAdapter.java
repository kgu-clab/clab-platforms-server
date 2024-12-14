package page.clab.api.domain.community.board.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.domain.community.board.application.port.out.RegisterBoardHashtagPort;
import page.clab.api.domain.community.board.domain.BoardHashtag;

@Component
@RequiredArgsConstructor
public class BoardHashtagPersistenceAdapter implements
        RegisterBoardHashtagPort {

    private final BoardHashtagRepository boardHashtagRepository;
    private final BoardHashtagMapper mapper;
    @Override
    public BoardHashtag save(BoardHashtag boardHashtag) {
        BoardHashtagJpaEntity entity = mapper.toEntity(boardHashtag);
        BoardHashtagJpaEntity savedEntity = boardHashtagRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }
}
