package page.clab.api.domain.members.blog.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.memberManagement.member.domain.Member;
import page.clab.api.domain.members.blog.application.dto.request.BlogUpdateRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Blog {

    private Long id;
    private String memberId;
    private String title;
    private String subTitle;
    private String content;
    private String imageUrl;
    private String hyperlink;
    private Boolean isDeleted;
    private LocalDateTime createdAt;

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
