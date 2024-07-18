package page.clab.api.domain.community.board.application.event;

public interface BoardEventProcessor {

    void processBoardDeleted(Long boardId);

    void processBoardUpdated(Long boardId);
}
