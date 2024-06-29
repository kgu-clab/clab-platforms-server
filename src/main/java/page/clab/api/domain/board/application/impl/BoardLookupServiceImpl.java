package page.clab.api.domain.board.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.board.application.BoardLookupService;
import page.clab.api.domain.board.dao.BoardRepository;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class BoardLookupServiceImpl implements BoardLookupService {

    private final BoardRepository boardRepository;

    @Override
    public Board getBoardByIdOrThrow(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException("[Board] id: " + boardId + "에 해당하는 게시글이 존재하지 않습니다."));
    }
}
