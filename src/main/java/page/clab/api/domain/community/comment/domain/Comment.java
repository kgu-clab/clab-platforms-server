package page.clab.api.domain.community.comment.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.community.comment.application.dto.request.CommentUpdateRequestDto;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.exception.PermissionDeniedException;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Comment {

    private Long id;
    private Long boardId;
    private String writerId;
    private String nickname;
    private String content;
    private Comment parent;

    @Builder.Default
    private List<Comment> children = new ArrayList<>();
    private boolean wantAnonymous;
    private Long likes;
    private Boolean isDeleted;
    private LocalDateTime createdAt;

    public void update(CommentUpdateRequestDto commentUpdateRequestDto) {
        Optional.ofNullable(commentUpdateRequestDto.getContent()).ifPresent(this::setContent);
        Optional.of(commentUpdateRequestDto.isWantAnonymous()).ifPresent(this::setWantAnonymous);
    }

    public void delete() {
        this.isDeleted = true;
    }

    public void addChildComment(Comment child) {
        this.children.add(child);
        child.setParent(this);
    }

    public boolean isOwner(String memberId) {
        return this.writerId.equals(memberId);
    }

    public void validateAccessPermission(MemberDetailedInfoDto memberInfo) throws PermissionDeniedException {
        if (!isOwner(memberInfo.getMemberId()) && !memberInfo.isAdminRole()) {
            throw new PermissionDeniedException("해당 댓글을 수정/삭제할 권한이 없습니다.");
        }
    }

    public void incrementLikes() {
        this.likes++;
    }

    public void decrementLikes() {
        if (this.likes > 0) {
            this.likes--;
        }
    }
}
