package page.clab.api.domain.comment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.comment.dao.CommentRepository;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.domain.comment.dto.response.CommentResponseDto;
import page.clab.api.domain.member.application.MemberLookupService;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FetchCommentsServiceImpl implements FetchCommentsService {

    private final CommentRepository commentRepository;
    private final MemberLookupService memberLookupService;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<CommentResponseDto> execute(Long boardId, Pageable pageable) {
        String currentMemberId = memberLookupService.getCurrentMemberId();
        Page<Comment> comments = getCommentByBoardIdAndParentIsNull(boardId, pageable);
        List<CommentResponseDto> commentDtos = comments.stream()
                .map(comment -> toCommentResponseDtoWithMemberInfo(comment, currentMemberId))
                .toList();
        return new PagedResponseDto<>(new PageImpl<>(commentDtos, pageable, comments.getTotalElements()));
    }

    private Page<Comment> getCommentByBoardIdAndParentIsNull(Long boardId, Pageable pageable) {
        return commentRepository.findAllByBoardIdAndParentIsNull(boardId, pageable);
    }

    private CommentResponseDto toCommentResponseDtoWithMemberInfo(Comment comment, String currentMemberId) {
        return CommentResponseDto.toDto(comment, memberLookupService.getMemberDetailedInfoById(comment.getWriterId()), comment.isOwner(currentMemberId), null);
    }
}
