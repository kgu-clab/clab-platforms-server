package page.clab.api.external.community.comment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.community.comment.application.port.out.RegisterCommentPort;
import page.clab.api.domain.community.comment.domain.Comment;
import page.clab.api.external.community.comment.application.port.ExternalRegisterCommentUseCase;

@Service
@RequiredArgsConstructor
public class ExternalCommentRegisterService implements ExternalRegisterCommentUseCase {

    private final RegisterCommentPort registerCommentPort;

    @Transactional
    @Override
    public Comment save(Comment comment) {
        return registerCommentPort.save(comment);
    }
}
