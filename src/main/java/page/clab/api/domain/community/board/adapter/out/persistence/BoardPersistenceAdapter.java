package page.clab.api.domain.community.board.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.community.board.application.port.out.RegisterBoardPort;
import page.clab.api.domain.community.board.application.port.out.RetrieveBoardPort;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.domain.community.board.domain.BoardCategory;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;

@Component
@RequiredArgsConstructor
public class BoardPersistenceAdapter implements
    RegisterBoardPort,
    RetrieveBoardPort {

    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;

    @Override
    public Board save(Board board) {
        BoardJpaEntity entity = boardMapper.toEntity(board);
        BoardJpaEntity savedEntity = boardRepository.save(entity);
        return boardMapper.toDomain(savedEntity);
    }

    @Override
    public Board getById(Long boardId) {
        return boardRepository.findById(boardId)
            .map(boardMapper::toDomain)
            .orElseThrow(
                () -> new BaseException(ErrorCode.NOT_FOUND, "[Board] id: " + boardId + "에 해당하는 게시글이 존재하지 않습니다."));
    }

    @Override
    public Board findByIdRegardlessOfDeletion(Long boardId) {
        return boardRepository.findByIdRegardlessOfDeletion(boardId)
            .map(boardMapper::toDomain)
            .orElseThrow(
                () -> new BaseException(ErrorCode.NOT_FOUND, "[Board] id: " + boardId + "에 해당하는 게시글이 존재하지 않습니다."));
    }

    @Override
    public List<Board> findAllWithinDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return boardRepository.findAllWithinDateRange(startDate, endDate).stream()
            .map(boardMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Board> findAll() {
        return boardRepository.findAll().stream()
            .map(boardMapper::toDomain)
            .collect(Collectors.toList());
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
        return boardRepository.findAllByMemberIdAndIsDeletedFalse(memberId, pageable)
            .map(boardMapper::toDomain);
    }

    @Override
    public Page<Board> findAll(Pageable pageable) {
        return boardRepository.findAll(pageable)
            .map(boardMapper::toDomain);
    }
}
