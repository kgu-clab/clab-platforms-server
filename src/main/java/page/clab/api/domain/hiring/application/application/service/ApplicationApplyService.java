package page.clab.api.domain.hiring.application.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.hiring.application.application.dto.request.ApplicationRequestDto;
import page.clab.api.domain.hiring.application.application.port.in.ApplyForApplicationUseCase;
import page.clab.api.domain.hiring.application.application.port.out.RegisterApplicationPort;
import page.clab.api.domain.hiring.application.domain.Application;
import page.clab.api.domain.hiring.recruitment.application.port.in.RetrieveRecruitmentUseCase;
import page.clab.api.domain.memberManagement.notification.application.port.in.SendNotificationUseCase;
import page.clab.api.global.common.slack.application.SlackService;

@Service
@RequiredArgsConstructor
public class ApplicationApplyService implements ApplyForApplicationUseCase {

    private final RetrieveRecruitmentUseCase retrieveRecruitmentUseCase;
    private final SendNotificationUseCase notificationService;
    private final SlackService slackService;
    private final RegisterApplicationPort registerApplicationPort;

    @Transactional
    @Override
    public String applyForClub(ApplicationRequestDto requestDto) {
        retrieveRecruitmentUseCase.findByIdOrThrow(requestDto.getRecruitmentId());
        Application application = ApplicationRequestDto.toEntity(requestDto);

        notificationService.sendNotificationToAdmins(requestDto.getStudentId() + " " +
                requestDto.getName() + "님이 동아리에 지원하였습니다.");
        slackService.sendNewApplicationNotification(requestDto);
        return registerApplicationPort.save(application).getStudentId();
    }
}
