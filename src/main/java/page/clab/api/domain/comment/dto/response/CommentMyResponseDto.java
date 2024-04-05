package page.clab.api.domain.comment.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.board.domain.BoardCategory;
import page.clab.api.domain.comment.domain.Comment;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentMyResponseDto {

    private Long id;

    private Long boardId;

    private BoardCategory boardCategory;

    private String writer;

    private String writerImageUrl;

    private String content;

    private Long likes;

    private boolean hasLikeByMe;

    private LocalDateTime createdAt;

    public static CommentMyResponseDto toDto(Comment comment, boolean hasLikeByMe) {
        return CommentMyResponseDto.builder()
                .id(comment.getId())
                .boardId(comment.getBoard().getId())
                .boardCategory(comment.getBoard().getCategory())
                .writer(comment.isWantAnonymous() ? comment.getNickname() : comment.getWriter().getName())
                .writerImageUrl(comment.isWantAnonymous() ? null : comment.getWriter().getImageUrl())
                .content(comment.getContent())
                .likes(comment.getLikes())
                .hasLikeByMe(hasLikeByMe)
                .createdAt(comment.getCreatedAt())
                .build();
    }

}
