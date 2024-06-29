package page.clab.api.domain.application.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.application.dao.ApplicationRepository;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.dto.request.ApplicationRequestDto;
import page.clab.api.domain.notification.application.NotificationSenderService;
import page.clab.api.domain.recruitment.application.RecruitmentLookupService;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
public class CreateApplicationServiceImpl implements CreateApplicationService {

    private final RecruitmentLookupService recruitmentLookupService;
    private final NotificationSenderService notificationService;
    private final ValidationService validationService;
    private final SlackService slackService;
    private final ApplicationRepository applicationRepository;

    @Transactional
    @Override
    public String execute(ApplicationRequestDto requestDto) {
        recruitmentLookupService.getRecruitmentByIdOrThrow(requestDto.getRecruitmentId());
        Application application = ApplicationRequestDto.toEntity(requestDto);
        validationService.checkValid(application);

        notificationService.sendNotificationToAdmins(requestDto.getStudentId() + " " +
                requestDto.getName() + "님이 동아리에 지원하였습니다.");
        slackService.sendNewApplicationNotification(requestDto);
        return applicationRepository.save(application).getStudentId();
    }
}