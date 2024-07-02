package page.clab.api.domain.board.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.board.domain.Board;

public interface RetrieveMyBoardsPort {
    Page<Board> findAllByMemberId(String memberId, Pageable pageable);
}
