package page.clab.api.domain.blog.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import page.clab.api.domain.blog.dto.request.BlogUpdateRequestDto;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.common.domain.BaseEntity;
import page.clab.api.global.exception.PermissionDeniedException;

import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE blog SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Blog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private String memberId;

    @Column(nullable = false)
    @Size(min = 1, max = 100, message = "{size.blog.title}")
    private String title;

    @Column(nullable = false)
    private String subTitle;

    @Column(nullable = false)
    @Size(min = 1, max = 10000, message = "{size.blog.content}")
    private String content;

    private String imageUrl;

    private String hyperlink;

    public void update(BlogUpdateRequestDto requestDto) {
        Optional.ofNullable(requestDto.getTitle()).ifPresent(this::setTitle);
        Optional.ofNullable(requestDto.getSubTitle()).ifPresent(this::setSubTitle);
        Optional.ofNullable(requestDto.getContent()).ifPresent(this::setContent);
        Optional.ofNullable(requestDto.getImageUrl()).ifPresent(this::setImageUrl);
        Optional.ofNullable(requestDto.getHyperlink()).ifPresent(this::setHyperlink);
    }

    public void delete() {
        this.isDeleted = true;
    }

    public boolean isOwner(String memberId) {
        return this.memberId.equals(memberId);
    }

    public void validateAccessPermission(Member member) throws PermissionDeniedException {
        if (!isOwner(member.getId()) && !member.isAdminRole()) {
            throw new PermissionDeniedException("해당 게시글을 수정/삭제할 권한이 없습니다.");
        }
    }

}
