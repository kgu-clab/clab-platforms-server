package page.clab.api.domain.notification.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import page.clab.api.domain.notification.application.port.out.RegisterNotificationPort;
import page.clab.api.domain.notification.application.port.out.RemoveNotificationPort;
import page.clab.api.domain.notification.application.port.out.RetrieveNotificationPort;
import page.clab.api.domain.notification.domain.Notification;
import page.clab.api.global.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NotificationPersistenceAdapter implements
        RegisterNotificationPort,
        RetrieveNotificationPort,
        RemoveNotificationPort {

    private final NotificationRepository repository;

    @Override
    public Notification save(Notification notification) {
        return repository.save(notification);
    }

    @Override
    public void saveAll(List<Notification> notifications) {
        repository.saveAll(notifications);
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Notification findByIdOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("[Notification] id: " + id + "에 해당하는 알림이 존재하지 않습니다."));
    }

    @Override
    public Page<Notification> findByMemberId(String memberId, Pageable pageable) {
        return repository.findByMemberId(memberId, pageable);
    }

    @Override
    public Page<Notification> findAllByIsDeletedTrue(Pageable pageable) {
        return repository.findAllByIsDeletedTrue(pageable);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
