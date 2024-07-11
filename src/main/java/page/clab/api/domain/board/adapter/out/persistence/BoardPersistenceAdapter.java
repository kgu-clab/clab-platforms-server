package page.clab.api.domain.board.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.board.application.port.out.RegisterBoardPort;
import page.clab.api.domain.board.application.port.out.RetrieveBoardPort;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.domain.BoardCategory;
import page.clab.api.global.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BoardPersistenceAdapter implements
        RegisterBoardPort,
        RetrieveBoardPort {

    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;

    @Override
    public Board save(Board board) {
        BoardJpaEntity entity = boardMapper.toJpaEntity(board);
        BoardJpaEntity savedEntity = boardRepository.save(entity);
        return boardMapper.toDomain(savedEntity);
    }

    @Override
    public void saveAll(List<Board> boards) {
        List<BoardJpaEntity> entities = boards.stream()
                .map(boardMapper::toJpaEntity)
                .toList();
        boardRepository.saveAll(entities);
    }

    @Override
    public Optional<Board> findById(Long boardId) {
        return boardRepository.findById(boardId)
                .map(boardMapper::toDomain);
    }

    @Override
    public Board findByIdOrThrow(Long boardId) {
        return boardRepository.findById(boardId)
                .map(boardMapper::toDomain)
                .orElseThrow(() -> new NotFoundException("[Board] id: " + boardId + "에 해당하는 게시글이 존재하지 않습니다."));
    }

    @Override
    public Page<Board> findAllByCategory(BoardCategory category, Pageable pageable) {
        return boardRepository.findAllByCategory(category, pageable)
                .map(boardMapper::toDomain);
    }

    @Override
    public Page<Board> findAllByIsDeletedTrue(Pageable pageable) {
        return boardRepository.findAllByIsDeletedTrue(pageable)
                .map(boardMapper::toDomain);
    }

    @Override
    public Page<Board> findAllByMemberId(String memberId, Pageable pageable) {
        return boardRepository.findAllByMemberId(memberId, pageable)
                .map(boardMapper::toDomain);
    }

    @Override
    public List<Board> findByMemberId(String memberId) {
        return boardRepository.findByMemberId(memberId).stream()
                .map(boardMapper::toDomain)
                .toList();
    }

    @Override
    public Page<Board> findAll(Pageable pageable) {
        return boardRepository.findAll(pageable)
                .map(boardMapper::toDomain);
    }
}
