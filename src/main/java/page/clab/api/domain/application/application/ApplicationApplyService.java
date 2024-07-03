package page.clab.api.domain.application.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.application.application.port.in.ApplicationApplyUseCase;
import page.clab.api.domain.application.application.port.out.RegisterApplicationPort;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.dto.request.ApplicationRequestDto;
import page.clab.api.domain.notification.application.port.in.NotificationSenderUseCase;
import page.clab.api.domain.recruitment.application.port.in.RecruitmentRetrievalUseCase;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class ApplicationApplyService implements ApplicationApplyUseCase {

    private final RecruitmentRetrievalUseCase recruitmentRetrievalUseCase;
    private final NotificationSenderUseCase notificationService;
    private final ValidationService validationService;
    private final SlackService slackService;
    private final RegisterApplicationPort registerApplicationPort;

    @Transactional
    @Override
    public String apply(ApplicationRequestDto requestDto) {
        recruitmentRetrievalUseCase.findByIdOrThrow(requestDto.getRecruitmentId());
        Application application = ApplicationRequestDto.toEntity(requestDto);
        validationService.checkValid(application);

        notificationService.sendNotificationToAdmins(requestDto.getStudentId() + " " +
                requestDto.getName() + "님이 동아리에 지원하였습니다.");
        slackService.sendNewApplicationNotification(requestDto);
        return registerApplicationPort.save(application).getStudentId();
    }
}
