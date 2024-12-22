package page.clab.api.domain.community.board.application.port.out;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.community.board.domain.BoardHashtag;

public interface RetrieveBoardHashtagPort {
    List<BoardHashtag> getAllByBoardId(Long boardId);

    List<BoardHashtag> getAllIncludingDeletedByBoardId(Long boardId);

    List<Long> getBoardIdsByHashTagId(List<Long> hashtagIds);
}
