package page.clab.api.domain.community.comment.application.event;

import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import page.clab.api.domain.community.board.application.event.BoardEventProcessor;
import page.clab.api.domain.community.board.application.event.BoardEventProcessorRegistry;
import page.clab.api.domain.community.comment.application.port.out.RegisterCommentPort;
import page.clab.api.domain.community.comment.application.port.out.RetrieveCommentPort;
import page.clab.api.domain.community.comment.domain.Comment;

@Component
@RequiredArgsConstructor
public class CommentEventProcessor implements BoardEventProcessor {

    private final RetrieveCommentPort retrieveCommentPort;
    private final RegisterCommentPort registerCommentPort;
    private final BoardEventProcessorRegistry processorRegistry;

    @PostConstruct
    public void init() {
        processorRegistry.register(this);
    }

    @Override
    public void processBoardDeleted(Long boardId) {
        List<Comment> comments = retrieveCommentPort.findByBoardId(boardId);
        comments.forEach(Comment::delete);
        registerCommentPort.saveAll(comments);
    }

    @Override
    public void processBoardUpdated(Long boardId) {
        // do nothing
    }
}
