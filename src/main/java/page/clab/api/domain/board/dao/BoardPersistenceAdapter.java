package page.clab.api.domain.board.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.board.application.port.out.RegisterBoardPort;
import page.clab.api.domain.board.application.port.out.RemoveBoardPort;
import page.clab.api.domain.board.application.port.out.RetrieveBoardPort;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.domain.BoardCategory;
import page.clab.api.global.exception.NotFoundException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BoardPersistenceAdapter implements
        RegisterBoardPort,
        RetrieveBoardPort,
        RemoveBoardPort {

    private final BoardRepository boardRepository;

    @Override
    public Board save(Board board) {
        return boardRepository.save(board);
    }

    @Override
    public void delete(Board board) {
        boardRepository.delete(board);
    }

    @Override
    public Optional<Board> findById(Long boardId) {
        return boardRepository.findById(boardId);
    }

    @Override
    public Board findByIdOrThrow(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException("[Board] id: " + boardId + "에 해당하는 게시글이 존재하지 않습니다."));
    }

    @Override
    public Page<Board> findAllByCategory(BoardCategory category, Pageable pageable) {
        return boardRepository.findAllByCategory(category, pageable);
    }

    @Override
    public Page<Board> findAllByIsDeletedTrue(Pageable pageable) {
        return boardRepository.findAllByIsDeletedTrue(pageable);
    }

    @Override
    public Page<Board> findAllByMemberId(String memberId, Pageable pageable) {
        return boardRepository.findAllByMemberId(memberId, pageable);
    }

    @Override
    public Page<Board> findAll(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }
}
