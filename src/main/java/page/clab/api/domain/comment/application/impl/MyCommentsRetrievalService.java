package page.clab.api.domain.comment.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.comment.application.MyCommentsRetrievalUseCase;
import page.clab.api.domain.comment.dao.CommentRepository;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.domain.comment.dto.response.CommentMyResponseDto;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MyCommentsRetrievalService implements MyCommentsRetrievalUseCase {

    private final CommentRepository commentRepository;
    private final MemberLookupUseCase memberLookupUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<CommentMyResponseDto> retrieve(Pageable pageable) {
        String currentMemberId = memberLookupUseCase.getCurrentMemberId();
        Page<Comment> comments = getCommentByWriterId(currentMemberId, pageable);
        List<CommentMyResponseDto> dtos = comments.stream()
                .map(comment -> toCommentMyResponseDto(comment, currentMemberId))
                .filter(Objects::nonNull)
                .toList();
        return new PagedResponseDto<>(new PageImpl<>(dtos, pageable, comments.getTotalElements()));
    }

    private Page<Comment> getCommentByWriterId(String memberId, Pageable pageable) {
        return commentRepository.findAllByWriterId(memberId, pageable);
    }

    private CommentMyResponseDto toCommentMyResponseDto(Comment comment, String currentMemberId) {
        return CommentMyResponseDto.toDto(comment, memberLookupUseCase.getMemberDetailedInfoById(comment.getWriterId()), false);
    }
}
