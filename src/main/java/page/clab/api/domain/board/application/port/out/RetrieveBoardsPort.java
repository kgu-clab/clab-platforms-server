package page.clab.api.domain.board.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.board.domain.Board;

public interface RetrieveBoardsPort {
    Page<Board> findAll(Pageable pageable);
}
