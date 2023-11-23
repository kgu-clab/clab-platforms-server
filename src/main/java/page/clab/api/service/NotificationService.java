package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.NotificationRepository;
import page.clab.api.type.dto.NotificationRequestDto;
import page.clab.api.type.dto.NotificationResponseDto;
import page.clab.api.type.dto.PagedResponseDto;
import page.clab.api.type.entity.Member;
import page.clab.api.type.entity.Notification;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final MemberService memberService;

    private final NotificationRepository notificationRepository;

    public void createNotification(NotificationRequestDto notificationRequestDto) {
        Member member = memberService.getMemberByIdOrThrow(notificationRequestDto.getMemberId());
        Notification notification = Notification.of(notificationRequestDto);
        notificationRepository.save(notification);
    }

    public PagedResponseDto<NotificationResponseDto> getNotifications(Pageable pageable) {
        Member member = memberService.getCurrentMember();
        Page<Notification> notifications = getNotificationByMember(pageable, member);
        return new PagedResponseDto<>(notifications.map(NotificationResponseDto::of));
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

    private Page<Notification> getNotificationByMember(Pageable pageable, Member member) {
        return notificationRepository.findByMemberOrderByCreatedAtDesc(member, pageable);
    }

}
