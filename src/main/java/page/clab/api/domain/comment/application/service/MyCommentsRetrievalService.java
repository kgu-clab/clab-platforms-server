package page.clab.api.domain.comment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.board.application.dto.shared.BoardCommentInfoDto;
import page.clab.api.domain.board.application.port.in.RetrieveBoardInfoUseCase;
import page.clab.api.domain.comment.application.dto.response.CommentMyResponseDto;
import page.clab.api.domain.comment.application.port.in.RetrieveMyCommentsUseCase;
import page.clab.api.domain.comment.application.port.out.RetrieveCommentPort;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.domain.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MyCommentsRetrievalService implements RetrieveMyCommentsUseCase {

    private final RetrieveCommentPort retrieveCommentPort;
    private final RetrieveMemberUseCase retrieveMemberUseCase;
    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;
    private final RetrieveBoardInfoUseCase retrieveBoardInfoUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<CommentMyResponseDto> retrieveMyComments(Pageable pageable) {
        String currentMemberId = retrieveMemberUseCase.getCurrentMemberId();
        Page<Comment> comments = retrieveCommentPort.findAllByWriterId(currentMemberId, pageable);
        List<CommentMyResponseDto> dtos = comments.stream()
                .map(this::toCommentMyResponseDto)
                .filter(Objects::nonNull)
                .toList();
        return new PagedResponseDto<>(new PageImpl<>(dtos, pageable, comments.getTotalElements()));
    }

    private CommentMyResponseDto toCommentMyResponseDto(Comment comment) {
        MemberDetailedInfoDto memberInfo = retrieveMemberInfoUseCase.getMemberDetailedInfoById(comment.getWriterId());
        BoardCommentInfoDto boardInfo = retrieveBoardInfoUseCase.getBoardCommentInfoById(comment.getBoardId());
        return CommentMyResponseDto.toDto(comment, memberInfo, boardInfo, false);
    }
}
