package page.clab.api.domain.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.comment.domain.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class CommentResponseDto {

    private Long id;

    private Boolean isDeleted;

    private String writerId;

    private String writerName;

    private String writerImageUrl;

    private Long writerRoleLevel;

    private String content;

    private List<CommentResponseDto> children;

    private Long likes;

    private Boolean hasLikeByMe;

    @JsonProperty("isOwner")
    private Boolean isOwner;

    private LocalDateTime createdAt;

    public static CommentResponseDto toDto(Comment comment, String currentMemberId) {
        if (comment.getIsDeleted()) {
            return CommentResponseDto.builder()
                    .id(comment.getId())
                    .isDeleted(true)
                    .children(comment.getChildren().stream()
                            .map(child -> CommentResponseDto.toDto(child, currentMemberId))
                            .toList())
                    .build();
        }
        return CommentResponseDto.builder()
                .id(comment.getId())
                .isDeleted(false)
                .writerId(comment.isWantAnonymous() ? null : comment.getWriter().getId())
                .writerName(comment.isWantAnonymous() ? comment.getNickname() : comment.getWriter().getName())
                .writerImageUrl(comment.isWantAnonymous() ? null : comment.getWriter().getImageUrl())
                .writerRoleLevel(comment.isWantAnonymous() ? null : comment.getWriter().getRole().toRoleLevel())
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
