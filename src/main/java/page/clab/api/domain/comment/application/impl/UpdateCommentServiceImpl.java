package page.clab.api.domain.comment.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.comment.application.UpdateCommentService;
import page.clab.api.domain.comment.dao.CommentRepository;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.domain.comment.dto.request.CommentUpdateRequestDto;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class UpdateCommentServiceImpl implements UpdateCommentService {

    private final CommentRepository commentRepository;
    private final MemberLookupService memberLookupService;
    private final ValidationService validationService;

    @Transactional
    @Override
    public Long execute(Long commentId, CommentUpdateRequestDto requestDto) throws PermissionDeniedException {
        Comment comment = getCommentByIdOrThrow(commentId);
        comment.validateAccessPermission(memberLookupService.getCurrentMemberDetailedInfo());
        comment.update(requestDto);
        validationService.checkValid(comment);
        commentRepository.save(comment);
        return comment.getBoard().getId();
    }

    private Comment getCommentByIdOrThrow(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));
    }
}