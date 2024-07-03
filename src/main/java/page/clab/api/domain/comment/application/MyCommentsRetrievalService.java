package page.clab.api.domain.comment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.comment.application.port.in.RetrieveMyCommentsUseCase;
import page.clab.api.domain.comment.application.port.out.RetrieveCommentsByWriterIdPort;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.domain.comment.dto.response.CommentMyResponseDto;
import page.clab.api.domain.member.application.port.in.RetrieveMemberInfoUseCase;
import page.clab.api.domain.member.application.port.in.RetrieveMemberUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MyCommentsRetrievalService implements RetrieveMyCommentsUseCase {

    private final RetrieveCommentsByWriterIdPort retrieveCommentsByWriterIdPort;
    private final RetrieveMemberUseCase retrieveMemberUseCase;
    private final RetrieveMemberInfoUseCase retrieveMemberInfoUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<CommentMyResponseDto> retrieve(Pageable pageable) {
        String currentMemberId = retrieveMemberUseCase.getCurrentMemberId();
        Page<Comment> comments = retrieveCommentsByWriterIdPort.findAllByWriterId(currentMemberId, pageable);
        List<CommentMyResponseDto> dtos = comments.stream()
                .map(this::toCommentMyResponseDto)
                .filter(Objects::nonNull)
                .toList();
        return new PagedResponseDto<>(new PageImpl<>(dtos, pageable, comments.getTotalElements()));
    }

    private CommentMyResponseDto toCommentMyResponseDto(Comment comment) {
        return CommentMyResponseDto.toDto(comment, retrieveMemberInfoUseCase.getMemberDetailedInfoById(comment.getWriterId()), false);
    }
}
