package page.clab.api.domain.recruitment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.notification.application.port.in.NotificationSenderUseCase;
import page.clab.api.domain.recruitment.application.port.in.RecruitmentRegisterUseCase;
import page.clab.api.domain.recruitment.application.port.out.RegisterRecruitmentPort;
import page.clab.api.domain.recruitment.domain.Recruitment;
import page.clab.api.domain.recruitment.dto.request.RecruitmentRequestDto;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class RecruitmentRegisterService implements RecruitmentRegisterUseCase {

    private final NotificationSenderUseCase notificationService;
    private final ValidationService validationService;
    private final RegisterRecruitmentPort registerRecruitmentPort;
    private final RecruitmentStatusUpdater recruitmentStatusUpdater;

    @Transactional
    @Override
    public Long register(RecruitmentRequestDto requestDto) {
        Recruitment recruitment = RecruitmentRequestDto.toEntity(requestDto);
        recruitmentStatusUpdater.updateRecruitmentStatusByRecruitment(recruitment);
        validationService.checkValid(recruitment);
        notificationService.sendNotificationToAllMembers("새로운 모집 공고가 등록되었습니다.");
        return registerRecruitmentPort.save(recruitment).getId();
    }
}
