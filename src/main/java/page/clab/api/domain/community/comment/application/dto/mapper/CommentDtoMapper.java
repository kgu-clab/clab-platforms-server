package page.clab.api.domain.community.comment.application.dto.mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import page.clab.api.domain.community.board.application.dto.shared.BoardCommentInfoDto;
import page.clab.api.domain.community.comment.application.dto.request.CommentRequestDto;
import page.clab.api.domain.community.comment.application.dto.response.CommentMyResponseDto;
import page.clab.api.domain.community.comment.application.dto.response.CommentResponseDto;
import page.clab.api.domain.community.comment.application.dto.response.DeletedCommentResponseDto;
import page.clab.api.domain.community.comment.domain.Comment;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.util.RandomNicknameUtil;

@Component
public class CommentDtoMapper {

    public Comment fromDto(CommentRequestDto requestDto, Long boardId, String writerId, Comment parent) {
        return Comment.builder()
            .boardId(boardId)
            .writerId(writerId)
            .nickname(RandomNicknameUtil.makeRandomNickname())
            .content(requestDto.getContent())
            .parent(parent)
            .wantAnonymous(requestDto.isWantAnonymous())
            .likes(0L)
            .isDeleted(false)
            .build();
    }

    public CommentMyResponseDto toDto(Comment comment, MemberDetailedInfoDto memberInfo, BoardCommentInfoDto boardInfo,
        boolean hasLikeByMe) {
        if (comment.getBoardId() == null || comment.getIsDeleted()) {
            return null;
        }
        return CommentMyResponseDto.builder()
            .id(comment.getId())
            .boardId(boardInfo.getBoardId())
            .boardCategory(boardInfo.getCategory().getKey())
            .writer(comment.isWantAnonymous() ? comment.getNickname() : memberInfo.getMemberName())
            .writerImageUrl(comment.isWantAnonymous() ? null : memberInfo.getImageUrl())
            .content(comment.getContent())
            .likes(comment.getLikes())
            .hasLikeByMe(hasLikeByMe)
            .createdAt(comment.getCreatedAt())
            .build();
    }

    public CommentResponseDto toDto(Comment comment, MemberDetailedInfoDto memberInfo, boolean isOwner,
        List<CommentResponseDto> children) {
        if (comment.getIsDeleted()) {
            return CommentResponseDto.builder()
                .id(comment.getId())
                .isDeleted(true)
                .children(children)
                .likes(comment.getLikes())
                .createdAt(comment.getCreatedAt())
                .build();
        }
        return CommentResponseDto.builder()
            .id(comment.getId())
            .isDeleted(false)
            .writerId(comment.isWantAnonymous() ? null : comment.getWriterId())
            .writerName(comment.isWantAnonymous() ? comment.getNickname() : memberInfo.getMemberName())
            .writerImageUrl(comment.isWantAnonymous() ? null : memberInfo.getImageUrl())
            .writerRoleLevel(comment.isWantAnonymous() ? null : memberInfo.getRoleLevel())
            .content(comment.getContent())
            .children(children)
            .likes(comment.getLikes())
            .isOwner(isOwner)
            .createdAt(comment.getCreatedAt())
            .build();
    }

    public DeletedCommentResponseDto toDto(Comment comment, MemberDetailedInfoDto memberInfo, boolean isOwner) {
        return DeletedCommentResponseDto.builder()
            .id(comment.getId())
            .writerId(comment.isWantAnonymous() ? null : memberInfo.getMemberId())
            .writerName(comment.isWantAnonymous() ? comment.getNickname() : memberInfo.getMemberName())
            .writerImageUrl(comment.isWantAnonymous() ? null : memberInfo.getImageUrl())
            .writerRoleLevel(comment.isWantAnonymous() ? null : memberInfo.getRoleLevel())
            .content(comment.getContent())
            .likes(comment.getLikes())
            .isOwner(isOwner)
            .createdAt(comment.getCreatedAt())
            .build();
    }
}
