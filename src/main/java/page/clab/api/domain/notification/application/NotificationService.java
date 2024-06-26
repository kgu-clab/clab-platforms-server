package page.clab.api.domain.notification.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.member.application.MemberLookupService;
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

    private final MemberLookupService memberLookupService;

    private final ValidationService validationService;

    private final NotificationRepository notificationRepository;

    @Transactional
    public Long createNotification(NotificationRequestDto requestDto) {
        memberLookupService.ensureMemberExists(requestDto.getMemberId());
        Notification notification = NotificationRequestDto.toEntity(requestDto);
        validationService.checkValid(notification);
        return notificationRepository.save(notification).getId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<NotificationResponseDto> getNotifications(Pageable pageable) {
        String currentMemberId = memberLookupService.getCurrentMemberId();
        Page<Notification> notifications = getNotificationByMemberId(currentMemberId, pageable);
        return new PagedResponseDto<>(notifications.map(NotificationResponseDto::toDto));
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<NotificationResponseDto> getDeletedNotifications(Pageable pageable) {
        Page<Notification> notifications = notificationRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(notifications.map(NotificationResponseDto::toDto));
    }

    @Transactional
    public Long deleteNotification(Long notificationId) throws PermissionDeniedException {
        String currentMemberId = memberLookupService.getCurrentMemberId();
        Notification notification = getNotificationByIdOrThrow(notificationId);
        notification.validateAccessPermission(currentMemberId);
        notificationRepository.delete(notification);
        return notification.getId();
    }

    public void sendNotificationToAllMembers(String content) {
        List<Notification> notifications = memberLookupService.findAllMembers().stream()
                .map(member -> Notification.create(member.getId(), content))
                .toList();
        notificationRepository.saveAll(notifications);
    }

    public void sendNotificationToMember(String memberId, String content) {
        memberLookupService.ensureMemberExists(memberId);
        Notification notification = Notification.create(memberId, content);
        notificationRepository.save(notification);
    }

    public void sendNotificationToMembers(List<String> memberIds, String content) {
        List<Notification> notifications = memberIds.stream()
                .map(memberId -> Notification.create(memberId, content))
                .toList();
        notificationRepository.saveAll(notifications);
    }

    public void sendNotificationToAdmins(String content) {
        sendNotificationToSpecificRole(memberLookupService::getAdminIds, content);
    }

    public void sendNotificationToSuperAdmins(String content) {
        sendNotificationToSpecificRole(memberLookupService::getSuperAdminIds, content);
    }

    private void sendNotificationToSpecificRole(Supplier<List<String>> memberSupplier, String content) {
        List<Notification> notifications = memberSupplier.get().stream()
                .map(memberId -> Notification.create(memberId, content))
                .toList();
        notificationRepository.saveAll(notifications);
    }

    private Notification getNotificationByIdOrThrow(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 알림입니다."));
    }

    private Page<Notification> getNotificationByMemberId(String memberId, Pageable pageable) {
        return notificationRepository.findByMemberId(memberId, pageable);
    }

}
