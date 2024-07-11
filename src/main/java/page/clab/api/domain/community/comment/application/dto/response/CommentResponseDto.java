package page.clab.api.domain.community.comment.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.community.comment.domain.Comment;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;

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

    public static CommentResponseDto toDto(Comment comment, MemberDetailedInfoDto memberInfo, boolean isOwner, List<CommentResponseDto> children) {
        if (comment.isDeleted()) {
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
}
