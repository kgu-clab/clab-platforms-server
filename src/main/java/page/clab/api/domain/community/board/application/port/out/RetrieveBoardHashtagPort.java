package page.clab.api.domain.community.board.application.port.out;

import java.util.List;
import page.clab.api.domain.community.board.domain.BoardHashtag;

public interface RetrieveBoardHashtagPort {

    List<BoardHashtag> findAllByBoardId(Long boardId);

    List<BoardHashtag> findAllIncludingDeletedByBoardId(Long boardId);

    List<Long> findBoardIdsByHashTagId(List<Long> hashtagIds);
}
