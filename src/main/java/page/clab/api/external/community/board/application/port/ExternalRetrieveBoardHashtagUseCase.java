package page.clab.api.external.community.board.application.port;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import page.clab.api.domain.community.board.application.dto.response.BoardHashtagResponseDto;
import page.clab.api.domain.community.board.domain.BoardHashtag;

public interface ExternalRetrieveBoardHashtagUseCase {
    List<BoardHashtagResponseDto> getBoardHashtagInfoByBoardId(Long boardId);

    List<BoardHashtag> getAllByBoardId(Long boardId);

    List<Long> extractAllHashtagId(List<BoardHashtag> boardHashtagList);

    List<BoardHashtag> getAllIncludingDeletedByBoardId(Long boardId);

    List<Long> getBoardIdsByHashTagId(List<Long> hashtagIds);
}
