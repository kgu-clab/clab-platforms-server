package page.clab.api.domain.application.application;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.application.dao.ApplicationRepository;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.domain.ApplicationId;
import page.clab.api.domain.application.dto.request.ApplicationRequestDto;
import page.clab.api.domain.application.dto.response.ApplicationPassResponseDto;
import page.clab.api.domain.application.dto.response.ApplicationResponseDto;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.domain.recruitment.application.RecruitmentService;
import page.clab.api.domain.recruitment.domain.Recruitment;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationService {

    private final RecruitmentService recruitmentService;

    private final NotificationService notificationService;

    private final SlackService slackService;

    private final ApplicationRepository applicationRepository;

    @Transactional
    public String createApplication(HttpServletRequest request, ApplicationRequestDto applicationRequestDto) {
        Recruitment recruitment = recruitmentService.getRecruitmentByIdOrThrow(applicationRequestDto.getRecruitmentId());
        Application application = Application.create(applicationRequestDto);
        notificationService.sendNotificationToAdmins(
                applicationRequestDto.getStudentId() + " " +
                        applicationRequestDto.getName() + "님이 동아리에 지원하였습니다."
        );
        slackService.sendApplicationNotification(request, applicationRequestDto);
        return applicationRepository.save(application).getStudentId();
    }

    public PagedResponseDto<ApplicationResponseDto> getApplicationsByConditions(Long recruitmentId, String studentId, Boolean isPass, Pageable pageable) {
        Page<Application> applications = applicationRepository.findByConditions(recruitmentId, studentId, isPass, pageable);
        return new PagedResponseDto<>(applications.map(ApplicationResponseDto::of));
    }

    public String approveApplication(Long recruitmentId, String studentId) {
        Application application = getApplicationByIdOrThrow(studentId, recruitmentId);
        application.toggleApprovalStatus();
        return applicationRepository.save(application).getStudentId();
    }

    public ApplicationPassResponseDto getApplicationPass(Long recruitmentId, String studentId) {
        Application application = getApplicationById(studentId, recruitmentId);
        if (application == null) {
            return ApplicationPassResponseDto.builder()
                    .isPass(false)
                    .build();
        }
        return ApplicationPassResponseDto.of(application);
    }

    public String deleteApplication(Long recruitmentId, String studentId) {
        Application application = getApplicationByIdOrThrow(studentId, recruitmentId);
        applicationRepository.delete(application);
        return application.getStudentId();
    }

    private Application getApplicationByIdOrThrow(String studentId, Long recruitmentId) {
        ApplicationId id = new ApplicationId(studentId, recruitmentId);
        return applicationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 지원자가 없습니다."));
    }

    private Application getApplicationById(String studentId, Long recruitmentId) {
        ApplicationId id = new ApplicationId(studentId, recruitmentId);
        return applicationRepository.findById(id)
                .orElse(null);
    }

}
