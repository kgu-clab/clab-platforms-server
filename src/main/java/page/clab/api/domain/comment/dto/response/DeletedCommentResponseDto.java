package page.clab.api.domain.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.comment.domain.Comment;

@Getter
@Setter
@Builder
public class DeletedCommentResponseDto {

    private Long id;

    private String writerId;

    private String writerName;

    private String writerImageUrl;

    private Long writerRoleLevel;

    private String content;

    private Long likes;

    @JsonProperty("isOwner")
    private boolean isOwner;

    private LocalDateTime createdAt;

    public static DeletedCommentResponseDto toDto(Comment comment, String currentMemberId) {
        return DeletedCommentResponseDto.builder()
                .id(comment.getId())
                .writerId(comment.isWantAnonymous() ? null : comment.getWriter().getId())
                .writerName(comment.isWantAnonymous() ? comment.getNickname() : comment.getWriter().getName())
                .writerImageUrl(comment.isWantAnonymous() ? null : comment.getWriter().getImageUrl())
                .writerRoleLevel(comment.isWantAnonymous() ? null : comment.getWriter().getRole().toRoleLevel())
                .content(comment.getContent())
                .likes(comment.getLikes())
                .isOwner(comment.isOwner(currentMemberId))
                .createdAt(comment.getCreatedAt())
                .build();
    }

}
