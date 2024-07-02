package page.clab.api.domain.comment.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.comment.application.DeletedCommentsRetrievalUseCase;
import page.clab.api.domain.comment.dao.CommentRepository;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.domain.comment.dto.response.DeletedCommentResponseDto;
import page.clab.api.domain.member.application.MemberLookupUseCase;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.common.dto.PagedResponseDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeletedCommentsRetrievalService implements DeletedCommentsRetrievalUseCase {

    private final CommentRepository commentRepository;
    private final MemberLookupUseCase memberLookupUseCase;

    @Transactional(readOnly = true)
    @Override
    public PagedResponseDto<DeletedCommentResponseDto> retrieve(Long boardId, Pageable pageable) {
        String currentMemberId = memberLookupUseCase.getCurrentMemberId();
        Page<Comment> comments = commentRepository.findAllByIsDeletedTrueAndBoardId(boardId, pageable);
        List<DeletedCommentResponseDto> deletedCommentDtos = comments.stream()
                .map(comment -> {
                    MemberDetailedInfoDto memberInfo = memberLookupUseCase.getMemberDetailedInfoById(comment.getWriterId());
                    return DeletedCommentResponseDto.toDto(comment, memberInfo, comment.isOwner(currentMemberId));
                })
                .toList();
        return new PagedResponseDto<>(new PageImpl<>(deletedCommentDtos, pageable, comments.getTotalElements()));
    }
}
