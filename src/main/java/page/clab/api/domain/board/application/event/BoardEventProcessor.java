package page.clab.api.domain.board.application.event;

public interface BoardEventProcessor {
    void processBoardDeleted(Long boardId);

    void processBoardUpdated(Long boardId);
}
