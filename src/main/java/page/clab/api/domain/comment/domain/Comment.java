package page.clab.api.domain.comment.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.comment.dto.request.CommentUpdateRequestDto;
import page.clab.api.domain.member.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.common.domain.BaseEntity;
import page.clab.api.global.exception.PermissionDeniedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(name = "member_id", nullable = false)
    private String writerId;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    @Size(min = 1, max = 1000, message = "{size.comment.content}")
    private String content;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonIgnoreProperties("children")
    private Comment parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    @JsonIgnoreProperties("parent")
    private List<Comment> children = new ArrayList<>();

    @Column(nullable = false)
    private boolean wantAnonymous;

    public void update(CommentUpdateRequestDto commentUpdateRequestDto) {
        Optional.ofNullable(commentUpdateRequestDto.getContent()).ifPresent(this::setContent);
        Optional.of(commentUpdateRequestDto.isWantAnonymous()).ifPresent(this::setWantAnonymous);
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

    public void delete() {
        this.isDeleted = true;
    }

}