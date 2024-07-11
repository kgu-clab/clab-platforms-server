package page.clab.api.domain.memberManagement.notification.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.global.exception.PermissionDeniedException;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Notification {

    private Long id;
    private String memberId;
    private String content;

    @Builder.Default
    private boolean isDeleted = false;
    private LocalDateTime createdAt;

    public static Notification create(String memberId, String content) {
        return Notification.builder()
                .memberId(memberId)
                .content(content)
                .build();
    }

    public void delete() {
        this.isDeleted = true;
    }

    public boolean isOwner(String memberId) {
        return this.memberId.equals(memberId);
    }

    public void validateAccessPermission(String memberId) throws PermissionDeniedException {
        if (!isOwner(memberId)) {
            throw new PermissionDeniedException("해당 알림을 수정/삭제할 권한이 없습니다.");
        }
    }
}
