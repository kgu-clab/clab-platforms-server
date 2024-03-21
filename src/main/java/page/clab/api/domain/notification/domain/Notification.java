package page.clab.api.domain.notification.domain;

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
import org.hibernate.annotations.CreationTimestamp;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.notification.dto.request.NotificationRequestDto;
import page.clab.api.global.exception.PermissionDeniedException;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 1, max = 1000, message = "{size.notification.content}")
    private String content;

    @ManyToOne()
    @JoinColumn(name = "member_id")
    private Member member;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public static Notification create(NotificationRequestDto notificationRequestDto, Member member) {
        return Notification.builder()
                .content(notificationRequestDto.getContent())
                .member(member)
                .build();
    }

    public static Notification create(Member member, String content) {
        return Notification.builder()
                .content(content)
                .member(member)
                .build();
    }

    public boolean isOwner(Member member) {
        return this.member.isSameMember(member);
    }

    public void validateAccessPermission(Member member) throws PermissionDeniedException {
        if (!isOwner(member)) {
            throw new PermissionDeniedException("해당 알림을 수정/삭제할 권한이 없습니다.");
        }
    }

}