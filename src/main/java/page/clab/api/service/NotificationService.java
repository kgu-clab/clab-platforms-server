package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.NotificationRepository;
import page.clab.api.type.dto.NotificationRequestDto;
import page.clab.api.type.dto.NotificationResponseDto;
import page.clab.api.type.entity.Member;
import page.clab.api.type.entity.Notification;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final MemberService memberService;

    private final NotificationRepository notificationRepository;

    public void createNotification(NotificationRequestDto notificationRequestDto) {
        Notification notification = Notification.of(notificationRequestDto);
        notificationRepository.save(notification);
    }

    public List<NotificationResponseDto> getNotifications() {
        Member member = memberService.getCurrentMember();
        List<Notification> notifications = notificationRepository.findByMember(member);
        return notifications.stream()
                .map(NotificationResponseDto::of)
                .collect(Collectors.toList());
    }

    public void deleteNotification(Long notificationId) throws PermissionDeniedException {
        Notification notification = getNotificationByIdOrThrow(notificationId);
        if (isNotificationOwner(notification)) {
            notificationRepository.delete(notification);
        } else {
            throw new PermissionDeniedException();
        }
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
