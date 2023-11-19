package page.clab.api.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.NotificationRepository;
import page.clab.api.type.dto.NotificationRequestDto;
import page.clab.api.type.dto.NotificationResponseDto;
import page.clab.api.type.entity.Member;
import page.clab.api.type.entity.Notification;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final MemberService memberService;

    private final NotificationRepository notificationRepository;

    public void createNotification(NotificationRequestDto notificationRequestDto) {
        Notification notification = Notification.of(notificationRequestDto);
        notificationRepository.save(notification);
    }

    public List<NotificationResponseDto> getNotifications(Pageable pageable) {
        Member member = memberService.getCurrentMember();
        Page<Notification> notifications = notificationRepository.findByMember(member, pageable);
        return notifications.map(NotificationResponseDto::of).getContent();
    }

    public void deleteNotification(Long notificationId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        Notification notification = getNotificationByIdOrThrow(notificationId);
        if (!member.equals(notification.getMember())) {
            throw new PermissionDeniedException();
        }
        notificationRepository.delete(notification);
    }

    private Notification getNotificationByIdOrThrow(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 알림입니다."));
    }

    private boolean isNotificationOwner(Notification notification) {
        Member member = memberService.getCurrentMember();
        if (member.equals(notification.getMember())) {
            return true;
        } else {
            return false;
        }
    }

}
