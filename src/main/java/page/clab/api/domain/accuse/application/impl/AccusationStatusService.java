package page.clab.api.domain.accuse.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.accuse.application.AccusationStatusUseCase;
import page.clab.api.domain.accuse.dao.AccuseRepository;
import page.clab.api.domain.accuse.dao.AccuseTargetRepository;
import page.clab.api.domain.accuse.domain.Accuse;
import page.clab.api.domain.accuse.domain.AccuseStatus;
import page.clab.api.domain.accuse.domain.AccuseTarget;
import page.clab.api.domain.accuse.domain.AccuseTargetId;
import page.clab.api.domain.accuse.domain.TargetType;
import page.clab.api.domain.notification.application.port.in.NotificationSenderUseCase;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.validation.ValidationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccusationStatusService implements AccusationStatusUseCase {

    private final AccuseTargetRepository accuseTargetRepository;
    private final AccuseRepository accuseRepository;
    private final ValidationService validationService;
    private final NotificationSenderUseCase notificationService;

    @Transactional
    @Override
    public Long change(TargetType type, Long targetId, AccuseStatus status) {
        AccuseTarget target = getAccuseTargetByIdOrThrow(type, targetId);
        target.updateStatus(status);
        validationService.checkValid(target);
        sendStatusUpdateNotifications(status, target);
        return accuseTargetRepository.save(target).getTargetReferenceId();
    }

    private AccuseTarget getAccuseTargetByIdOrThrow(TargetType type, Long targetId) {
        return accuseTargetRepository.findById(AccuseTargetId.create(type, targetId))
                .orElseThrow(() -> new NotFoundException("존재하지 않는 신고 대상입니다."));
    }

    private void sendStatusUpdateNotifications(AccuseStatus status, AccuseTarget target) {
        List<String> memberIds = accuseRepository.findByTarget(target.getTargetType(), target.getTargetReferenceId()).stream()
                .map(Accuse::getMemberId)
                .collect(Collectors.toList());
        notificationService.sendNotificationToMembers(memberIds, "신고 상태가 " + status.getDescription() + "(으)로 변경되었습니다.");
    }
}