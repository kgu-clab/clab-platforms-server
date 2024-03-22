package page.clab.api.domain.blog.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.blog.dto.request.BlogUpdateRequestDto;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.global.common.domain.BaseEntity;
import page.clab.api.global.exception.PermissionDeniedException;

import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Blog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    @Size(min = 1, max = 100, message = "{size.blog.title}")
    private String title;

    @Column(nullable = false)
    private String subTitle;

    @Column(nullable = false)
    @Size(min = 1, max = 10000, message = "{size.blog.content}")
    private String content;

    private String imageUrl;

    public void update(BlogUpdateRequestDto requestDto) {
        Optional.ofNullable(requestDto.getTitle()).ifPresent(this::setTitle);
        Optional.ofNullable(requestDto.getSubTitle()).ifPresent(this::setSubTitle);
        Optional.ofNullable(requestDto.getContent()).ifPresent(this::setContent);
        Optional.ofNullable(requestDto.getImageUrl()).ifPresent(this::setImageUrl);
    }

    public boolean isOwner(Member member) {
        return this.member.isSameMember(member);
    }

    public void validateAccessPermission(Member member) throws PermissionDeniedException {
        if (!isOwner(member) && !member.isAdminRole()) {
            throw new PermissionDeniedException("해당 게시글을 수정/삭제할 권한이 없습니다.");
        }
    }

}
