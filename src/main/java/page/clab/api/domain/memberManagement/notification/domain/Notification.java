package page.clab.api.domain.memberManagement.notification.domain;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Notification {

    private Long id;
    private String memberId;
    private String content;
    private Boolean isDeleted;
    private LocalDateTime createdAt;

    public static Notification create(String memberId, String content) {
        return Notification.builder()
            .memberId(memberId)
            .content(content)
            .isDeleted(false)
            .build();
    }

    public void delete() {
        this.isDeleted = true;
    }
}
