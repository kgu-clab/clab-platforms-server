package page.clab.api.domain.activity.review.domain;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.activity.activitygroup.domain.ActivityGroup;
import page.clab.api.domain.activity.review.application.dto.request.ReviewUpdateRequestDto;
import page.clab.api.domain.memberManagement.member.application.dto.shared.MemberDetailedInfoDto;
import page.clab.api.global.exception.BaseException;
import page.clab.api.global.exception.ErrorCode;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Review {

    private Long id;
    private ActivityGroup activityGroup;
    private String memberId;
    private String content;
    private Boolean isPublic;
    private Boolean isDeleted;
    private LocalDateTime createdAt;

    public void update(ReviewUpdateRequestDto requestDto) {
        Optional.ofNullable(requestDto.getContent()).ifPresent(this::setContent);
        Optional.ofNullable(requestDto.getIsPublic()).ifPresent(this::setIsPublic);
    }

    public void delete() {
        this.isDeleted = true;
    }

    public boolean isOwner(String memberId) {
        return this.memberId.equals(memberId);
    }

    public void validateAccessPermission(MemberDetailedInfoDto memberInfo) {
        if (!isOwner(memberInfo.getMemberId()) && !memberInfo.isAdminRole()) {
            throw new BaseException(ErrorCode.PERMISSION_DENIED, "해당 후기를 수정/삭제할 권한이 없습니다.");
        }
    }
}
