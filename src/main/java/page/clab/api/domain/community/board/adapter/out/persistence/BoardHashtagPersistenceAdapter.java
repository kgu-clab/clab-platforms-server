package page.clab.api.domain.community.board.adapter.out.persistence;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.domain.community.board.application.port.out.RegisterBoardHashtagPort;
import page.clab.api.domain.community.board.application.port.out.RetrieveBoardHashtagPort;
import page.clab.api.domain.community.board.domain.BoardHashtag;

@Component
@RequiredArgsConstructor
public class BoardHashtagPersistenceAdapter implements
        RegisterBoardHashtagPort, RetrieveBoardHashtagPort {

    private final BoardHashtagRepository boardHashtagRepository;
    private final BoardHashtagMapper mapper;
    @Override
    public BoardHashtag save(BoardHashtag boardHashtag) {
        BoardHashtagJpaEntity entity = mapper.toEntity(boardHashtag);
        BoardHashtagJpaEntity savedEntity = boardHashtagRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public List<BoardHashtag> getAllByBoardId(Long boardId) {
        return boardHashtagRepository.findAllByBoardId(boardId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<BoardHashtag> getAllIncludingDeletedByBoardId(Long boardId) {
        return boardHashtagRepository.findAllIncludingDeletedByBoardId(boardId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
