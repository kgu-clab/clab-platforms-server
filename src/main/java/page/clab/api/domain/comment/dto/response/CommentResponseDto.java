package page.clab.api.domain.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import page.clab.api.domain.comment.domain.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CommentResponseDto {

    private Long id;

    private String writer;

    private String writerImageUrl;

    private Long writerRoleLevel;

    private String content;

    private List<CommentResponseDto> children;

    private Long likes;

    private boolean hasLikeByMe;

    @JsonProperty("isOwner")
    private boolean isOwner;

    private LocalDateTime createdAt;

    public static CommentResponseDto toDto(Comment comment, String currentMemberId) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .writer(comment.isWantAnonymous() ? comment.getNickname() : comment.getWriter().getName())
                .writerImageUrl(comment.isWantAnonymous() ? null : comment.getWriter().getImageUrl())
                .writerRoleLevel(comment.getWriter().getRole().toRoleLevel())
                .content(comment.getContent())
                .children(comment.getChildren().stream()
                        .map(child -> CommentResponseDto.toDto(child, currentMemberId))
                        .toList())
                .likes(comment.getLikes())
                .isOwner(comment.isOwner(currentMemberId))
                .createdAt(comment.getCreatedAt())
                .build();
    }

}
