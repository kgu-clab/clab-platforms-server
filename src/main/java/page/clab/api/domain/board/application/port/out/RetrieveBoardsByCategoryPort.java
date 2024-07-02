package page.clab.api.domain.board.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.board.domain.BoardCategory;

public interface RetrieveBoardsByCategoryPort {
    Page<Board> findAllByCategory(BoardCategory category, Pageable pageable);
}