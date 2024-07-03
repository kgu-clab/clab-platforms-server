package page.clab.api.domain.accuse.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.accuse.application.port.in.ChangeAccusationStatusUseCase;
import page.clab.api.domain.accuse.application.port.out.LoadAccuseTargetPort;
import page.clab.api.domain.accuse.application.port.out.RegisterAccuseTargetPort;
import page.clab.api.domain.accuse.application.port.out.RetrieveAccuseByTargetPort;
import page.clab.api.domain.accuse.domain.Accuse;
import page.clab.api.domain.accuse.domain.AccuseStatus;
import page.clab.api.domain.accuse.domain.AccuseTarget;
import page.clab.api.domain.accuse.domain.AccuseTargetId;
import page.clab.api.domain.accuse.domain.TargetType;
import page.clab.api.domain.notification.application.port.in.SendNotificationUseCase;
import page.clab.api.global.validation.ValidationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccusationStatusService implements ChangeAccusationStatusUseCase {

    private final LoadAccuseTargetPort loadAccuseTargetPort;
    private final RetrieveAccuseByTargetPort retrieveAccuseByTargetPort;
    private final RegisterAccuseTargetPort registerAccuseTargetPort;
    private final ValidationService validationService;
    private final SendNotificationUseCase notificationService;

    @Transactional
    @Override
    public Long changeAccusationStatus(TargetType type, Long targetId, AccuseStatus status) {
        AccuseTarget target = loadAccuseTargetPort.findByIdOrThrow(AccuseTargetId.create(type, targetId));
        target.updateStatus(status);
        validationService.checkValid(target);
        sendStatusUpdateNotifications(status, target);
        return registerAccuseTargetPort.save(target).getTargetReferenceId();
    }

    private void sendStatusUpdateNotifications(AccuseStatus status, AccuseTarget target) {
        List<String> memberIds = retrieveAccuseByTargetPort.findByTarget(target.getTargetType(), target.getTargetReferenceId()).stream()
                .map(Accuse::getMemberId)
                .collect(Collectors.toList());
        notificationService.sendNotificationToMembers(memberIds, "신고 상태가 " + status.getDescription() + "(으)로 변경되었습니다.");
    }
}
