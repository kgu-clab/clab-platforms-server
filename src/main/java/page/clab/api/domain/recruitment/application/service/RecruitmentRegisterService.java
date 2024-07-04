package page.clab.api.domain.recruitment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.notification.application.port.in.SendNotificationUseCase;
import page.clab.api.domain.recruitment.application.port.in.RegisterRecruitmentUseCase;
import page.clab.api.domain.recruitment.application.port.out.RegisterRecruitmentPort;
import page.clab.api.domain.recruitment.domain.Recruitment;
import page.clab.api.domain.recruitment.dto.request.RecruitmentRequestDto;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class RecruitmentRegisterService implements RegisterRecruitmentUseCase {

    private final SendNotificationUseCase notificationService;
    private final ValidationService validationService;
    private final RegisterRecruitmentPort registerRecruitmentPort;
    private final RecruitmentStatusUpdater recruitmentStatusUpdater;

    @Transactional
    @Override
    public Long registerRecruitment(RecruitmentRequestDto requestDto) {
        Recruitment recruitment = RecruitmentRequestDto.toEntity(requestDto);
        recruitmentStatusUpdater.updateRecruitmentStatusByRecruitment(recruitment);
        validationService.checkValid(recruitment);
        notificationService.sendNotificationToAllMembers("새로운 모집 공고가 등록되었습니다.");
        return registerRecruitmentPort.save(recruitment).getId();
    }
}
