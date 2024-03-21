package page.clab.api.domain.notification.application;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.notification.dao.NotificationRepository;
import page.clab.api.domain.notification.domain.Notification;
import page.clab.api.domain.notification.dto.request.NotificationRequestDto;
import page.clab.api.domain.notification.dto.response.NotificationResponseDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.exception.PermissionDeniedException;
import page.clab.api.global.validation.ValidationService;

import java.util.List;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private MemberService memberService;

    private final ValidationService validationService;

    private final NotificationRepository notificationRepository;

    @Autowired
    public void setMemberService(@Lazy MemberService memberService) {
        this.memberService = memberService;
    }

    public Long createNotification(NotificationRequestDto notificationRequestDto) {
        Member member = memberService.getMemberByIdOrThrow(notificationRequestDto.getMemberId());
        Notification notification = Notification.create(notificationRequestDto, member);
        validationService.checkValid(notification);
        return notificationRepository.save(notification).getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<NotificationResponseDto> getNotifications(Pageable pageable) {
        Member member = memberService.getCurrentMember();
        Page<Notification> notifications = getNotificationByMember(member, pageable);
        return new PagedResponseDto<>(notifications.map(NotificationResponseDto::of));
    }

    public Long deleteNotification(Long notificationId) throws PermissionDeniedException {
        Member member = memberService.getCurrentMember();
        Notification notification = getNotificationByIdOrThrow(notificationId);
        notification.validateAccessPermission(member);
        notificationRepository.delete(notification);
        return notification.getId();
    }

    public void sendNotificationToAllMembers(String content) {
        List<Notification> notifications = memberService.findAll().stream()
                .map(member -> Notification.create(member, content))
                .toList();
        notificationRepository.saveAll(notifications);
    }

    public void sendNotificationToMember(Member member, String content) {
        Notification notification = Notification.create(member, content);
        notificationRepository.save(notification);
    }

    public void sendNotificationToMember(String memberId, String content) {
        Member member = memberService.getMemberByIdOrThrow(memberId);
        Notification notification = Notification.create(member, content);
        notificationRepository.save(notification);
    }

    public void sendNotificationToAdmins(String content) {
        sendNotificationToSpecificRole(memberService::getAdmins, content);
    }

    public void sendNotificationToSuperAdmins(String content) {
        sendNotificationToSpecificRole(memberService::getSuperAdmins, content);
    }

    private void sendNotificationToSpecificRole(Supplier<List<Member>> memberSupplier, String content) {
        List<Notification> notifications = memberSupplier.get().stream()
                .map(member -> Notification.create(member, content))
                .toList();
        notificationRepository.saveAll(notifications);
    }

    private Notification getNotificationByIdOrThrow(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 알림입니다."));
    }

    private Page<Notification> getNotificationByMember(Member member, Pageable pageable) {
        return notificationRepository.findByMemberOrderByCreatedAtDesc(member, pageable);
    }

}
