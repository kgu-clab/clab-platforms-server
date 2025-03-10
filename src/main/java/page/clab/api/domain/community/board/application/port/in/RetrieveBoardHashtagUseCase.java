package page.clab.api.domain.community.board.application.port.in;

import java.util.List;
import page.clab.api.domain.community.board.application.dto.response.BoardHashtagResponseDto;
import page.clab.api.domain.community.board.domain.BoardHashtag;

public interface RetrieveBoardHashtagUseCase {

    List<BoardHashtagResponseDto> getBoardHashtagInfoByBoardId(Long boardId);

    List<BoardHashtag> getAllByBoardId(Long boardId);

    List<Long> extractAllHashtagId(List<BoardHashtag> boardHashtagList);

    List<BoardHashtag> getAllIncludingDeletedByBoardId(Long boardId);

    List<Long> getBoardIdsByHashTagId(List<Long> hashtagIds);
}
