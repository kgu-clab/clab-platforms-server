package page.clab.api.domain.community.board.application.port.out;

import page.clab.api.domain.community.board.domain.BoardHashtag;

public interface RegisterBoardHashtagPort {

    BoardHashtag save(BoardHashtag boardHashtag);
}
