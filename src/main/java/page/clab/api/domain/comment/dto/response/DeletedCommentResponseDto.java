package page.clab.api.domain.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.comment.domain.Comment;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;

import java.time.LocalDateTime;

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

    public static DeletedCommentResponseDto toDto(Comment comment, MemberDetailedInfoDto memberInfo, boolean isOwner) {
        return DeletedCommentResponseDto.builder()
                .id(comment.getId())
                .writerId(comment.isWantAnonymous() ? null : memberInfo.getMemberId())
                .writerName(comment.isWantAnonymous() ? comment.getNickname() : memberInfo.getMemberName())
                .writerImageUrl(comment.isWantAnonymous() ? null : memberInfo.getImageUrl())
                .writerRoleLevel(comment.isWantAnonymous() ? null : memberInfo.getRoleLevel())
                .content(comment.getContent())
                .isOwner(isOwner)
                .createdAt(comment.getCreatedAt())
                .build();
    }

}
