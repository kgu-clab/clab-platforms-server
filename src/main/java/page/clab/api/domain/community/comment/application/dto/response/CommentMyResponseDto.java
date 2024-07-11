package page.clab.api.domain.community.comment.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.community.board.application.dto.shared.BoardCommentInfoDto;
import page.clab.api.domain.community.comment.domain.Comment;
import page.clab.api.domain.member.application.dto.shared.MemberDetailedInfoDto;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentMyResponseDto {

    private Long id;
    private Long boardId;
    private String boardCategory;
    private String writer;
    private String writerImageUrl;
    private String content;
    private Long likes;
    private boolean hasLikeByMe;
    private LocalDateTime createdAt;

    public static CommentMyResponseDto toDto(Comment comment, MemberDetailedInfoDto memberInfo, BoardCommentInfoDto boardInfo, boolean hasLikeByMe) {
        if (comment.getBoardId() == null || comment.isDeleted()) {
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
}
