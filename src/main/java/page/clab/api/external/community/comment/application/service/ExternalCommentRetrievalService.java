package page.clab.api.external.community.comment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.domain.community.comment.application.port.in.RetrieveCommentUseCase;
import page.clab.api.domain.community.comment.application.port.out.RetrieveCommentPort;
import page.clab.api.domain.community.comment.domain.Comment;
import page.clab.api.external.community.comment.application.port.ExternalRetrieveCommentUseCase;

@Service
@RequiredArgsConstructor
public class ExternalCommentRetrievalService implements ExternalRetrieveCommentUseCase {

    private final RetrieveCommentUseCase retrieveCommentUseCase;
    private final RetrieveCommentPort retrieveCommentPort;

    @Override
    public Comment findByIdOrThrow(Long targetId) {
        return retrieveCommentUseCase.findByIdOrThrow(targetId);
    }

    @Override
    public Long countByBoardId(Long id) {
        return retrieveCommentPort.countByBoardId(id);
    }

    @Override
    public long countCommentsByBoardId(Long id) {
        return retrieveCommentPort.countAllByBoardId(id);
    }
}
