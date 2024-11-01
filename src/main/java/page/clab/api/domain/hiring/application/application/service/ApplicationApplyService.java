package page.clab.api.domain.hiring.application.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.hiring.application.application.dto.mapper.ApplicationDtoMapper;
import page.clab.api.domain.hiring.application.application.dto.request.ApplicationRequestDto;
import page.clab.api.domain.hiring.application.application.port.in.ApplyForApplicationUseCase;
import page.clab.api.domain.hiring.application.application.port.out.RegisterApplicationPort;
import page.clab.api.domain.hiring.application.domain.Application;
import page.clab.api.external.hiring.application.application.port.ExternalRetrieveRecruitmentUseCase;
import page.clab.api.external.memberManagement.notification.application.port.ExternalSendNotificationUseCase;
import page.clab.api.global.common.slack.application.SlackService;

@Service
@RequiredArgsConstructor
public class ApplicationApplyService implements ApplyForApplicationUseCase {

    private final RegisterApplicationPort registerApplicationPort;
    private final ExternalRetrieveRecruitmentUseCase externalRetrieveRecruitmentUseCase;
    private final ExternalSendNotificationUseCase externalSendNotificationUseCase;
    private final SlackService slackService;
    private final ApplicationDtoMapper mapper;

    // 동아리 지원 신청을 처리합니다.
    // 지원자에 대한 알림을 관리자에게 전송하고, Slack에도 알림을 보냅니다.
    @Transactional
    @Override
    public String applyForClub(ApplicationRequestDto requestDto) {
        externalRetrieveRecruitmentUseCase.validateRecruitmentForApplication(requestDto.getRecruitmentId());
        Application application = mapper.fromDto(requestDto);
        String applicationType = application.getApplicationTypeForNotificationPrefix();
        externalSendNotificationUseCase.sendNotificationToAdmins(applicationType + requestDto.getStudentId() + " " +
                requestDto.getName() + "님이 지원하였습니다.");
        slackService.sendNewApplicationNotification(requestDto);
        return registerApplicationPort.save(application).getStudentId();
    }
}
