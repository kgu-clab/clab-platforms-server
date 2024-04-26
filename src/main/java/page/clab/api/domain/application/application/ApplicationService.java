package page.clab.api.domain.application.application;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import page.clab.api.domain.application.dao.ApplicationRepository;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.domain.ApplicationId;
import page.clab.api.domain.application.dto.request.ApplicationRequestDto;
import page.clab.api.domain.application.dto.response.ApplicationPassResponseDto;
import page.clab.api.domain.application.dto.response.ApplicationResponseDto;
import page.clab.api.domain.board.domain.Board;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.domain.recruitment.application.RecruitmentService;
import page.clab.api.domain.recruitment.domain.Recruitment;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.exception.NotFoundException;
import page.clab.api.global.validation.ValidationService;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationService {

    private final RecruitmentService recruitmentService;

    private final NotificationService notificationService;

    private final ValidationService validationService;

    private final SlackService slackService;

    private final ApplicationRepository applicationRepository;

    @Transactional
    public String createApplication(HttpServletRequest request, ApplicationRequestDto requestDto) {
        Recruitment recruitment = recruitmentService.getRecruitmentByIdOrThrow(requestDto.getRecruitmentId());
        Application application = ApplicationRequestDto.toEntity(requestDto);
        validationService.checkValid(application);

        notificationService.sendNotificationToAdmins(requestDto.getStudentId() + " " +
                        requestDto.getName() + "님이 동아리에 지원하였습니다.");
        slackService.sendApplicationNotification(request, requestDto);
        return applicationRepository.save(application).getStudentId();
    }

    @Transactional(readOnly = true)
    public PagedResponseDto<ApplicationResponseDto> getApplicationsByConditions(Long recruitmentId, String studentId, Boolean isPass, Pageable pageable) {
        Page<Application> applications = applicationRepository.findByConditions(recruitmentId, studentId, isPass, pageable);
        return new PagedResponseDto<>(applications.map(ApplicationResponseDto::toDto));
    }

    @Transactional
    public String toggleApprovalStatus(Long recruitmentId, String studentId) {
        Application application = getApplicationByIdOrThrow(studentId, recruitmentId);
        application.toggleApprovalStatus();
        validationService.checkValid(application);
        return applicationRepository.save(application).getStudentId();
    }

    @Transactional(readOnly = true)
    public ApplicationPassResponseDto getApplicationPass(Long recruitmentId, String studentId) {
        ApplicationId id = ApplicationId.create(studentId, recruitmentId);
        return applicationRepository.findById(id)
                .map(ApplicationPassResponseDto::toDto)
                .orElseGet(() -> ApplicationPassResponseDto.builder()
                        .isPass(false)
                        .build());
    }

    public String deleteApplication(Long recruitmentId, String studentId) {
        Application application = getApplicationByIdOrThrow(studentId, recruitmentId);
        applicationRepository.delete(application);
        return application.getStudentId();
    }

    public PagedResponseDto<ApplicationResponseDto> getDeletedApplications(Pageable pageable) {
        Page<Application> applications = applicationRepository.findAllByIsDeletedTrue(pageable);
        return new PagedResponseDto<>(applications.map(ApplicationResponseDto::toDto));
    }

    private Application getApplicationByIdOrThrow(String studentId, Long recruitmentId) {
        ApplicationId id = ApplicationId.create(studentId, recruitmentId);
        return applicationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 지원자가 없습니다."));
    }

}
