package page.clab.api.domain.community.board.application.port.out;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.community.board.domain.Board;
import page.clab.api.domain.community.board.domain.BoardCategory;

public interface RetrieveBoardPort {

    Board getById(Long boardId);

    Board findByIdRegardlessOfDeletion(Long boardId);

    List<Board> findAllWithinDateRange(LocalDateTime startDate, LocalDateTime endDate);

    List<Board> findAll();

    Page<Board> findAll(Pageable pageable);

    Page<Board> findAllByCategory(BoardCategory category, Pageable pageable);

    Page<Board> findAllByIsDeletedTrue(Pageable pageable);

    Page<Board> findAllByMemberId(String memberId, Pageable pageable);
}
