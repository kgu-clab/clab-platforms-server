package page.clab.api.domain.hiring.recruitment.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.hiring.recruitment.application.dto.request.RecruitmentRequestDto;
import page.clab.api.domain.hiring.recruitment.application.port.in.RegisterRecruitmentUseCase;
import page.clab.api.domain.hiring.recruitment.application.port.out.RegisterRecruitmentPort;
import page.clab.api.domain.hiring.recruitment.domain.Recruitment;
import page.clab.api.domain.memberManagement.notification.application.port.in.SendNotificationUseCase;

@Service
@RequiredArgsConstructor
public class RecruitmentRegisterService implements RegisterRecruitmentUseCase {

    private final SendNotificationUseCase notificationService;
    private final RegisterRecruitmentPort registerRecruitmentPort;
    private final RecruitmentStatusUpdater recruitmentStatusUpdater;

    @Transactional
    @Override
    public Long registerRecruitment(RecruitmentRequestDto requestDto) {
        Recruitment recruitment = RecruitmentRequestDto.toEntity(requestDto);
        recruitmentStatusUpdater.updateRecruitmentStatus(recruitment);
        recruitment.validateBusinessRules();
        notificationService.sendNotificationToAllMembers("새로운 모집 공고가 등록되었습니다.");
        return registerRecruitmentPort.save(recruitment).getId();
    }
}
