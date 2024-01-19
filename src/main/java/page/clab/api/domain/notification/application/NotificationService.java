package page.clab.api.domain.notification.application;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.domain.notification.dao.NotificationRepository;
import page.clab.api.domain.notification.dto.request.NotificationRequestDto;
import page.clab.api.domain.notification.dto.response.NotificationResponseDto;
import page.clab.api.global.dto.PagedResponseDto;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.notification.domain.Notification;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private MemberService memberService;

    private final NotificationRepository notificationRepository;

    @Autowired
    public void setMemberService(@Lazy MemberService memberService) {
        this.memberService = memberService;
    }

    public Long createNotification(NotificationRequestDto notificationRequestDto) {
        Member member = memberService.getMemberByIdOrThrow(notificationRequestDto.getMemberId());
        Notification notification = Notification.of(notificationRequestDto);
        return notificationRepository.save(notification).getId();
    }

    public PagedResponseDto<NotificationResponseDto> getNotifications(Pageable pageable) {
        Member member = memberService.getCurrentMember();
        Page<Notification> notifications = getNotificationByMember(pageable, member);
        return new PagedResponseDto<>(notifications.map(NotificationResponseDto::of));
    }

    public Long deleteNotification(Long notificationId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        Notification notification = getNotificationByIdOrThrow(notificationId);
        if (!member.equals(notification.getMember())) {
            throw new PermissionDeniedException();
        }
        notificationRepository.delete(notification);
        return notification.getId();
    }

    private Notification getNotificationByIdOrThrow(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 알림입니다."));
    }

    private Page<Notification> getNotificationByMember(Pageable pageable, Member member) {
        return notificationRepository.findByMemberOrderByCreatedAtDesc(member, pageable);
    }

}
