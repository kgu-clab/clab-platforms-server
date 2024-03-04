package page.clab.api.domain.application.application;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import page.clab.api.domain.application.dao.ApplicationRepository;
import page.clab.api.domain.application.domain.Application;
import page.clab.api.domain.application.dto.request.ApplicationRequestDto;
import page.clab.api.domain.application.dto.response.ApplicationPassResponseDto;
import page.clab.api.domain.application.dto.response.ApplicationResponseDto;
import page.clab.api.domain.member.application.MemberService;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.domain.Role;
import page.clab.api.domain.notification.application.NotificationService;
import page.clab.api.domain.notification.dto.request.NotificationRequestDto;
import page.clab.api.global.common.dto.PagedResponseDto;
import page.clab.api.global.common.slack.application.SlackService;
import page.clab.api.global.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationService {

    private final MemberService memberService;

    private final NotificationService notificationService;

    private final SlackService slackService;

    private final ApplicationRepository applicationRepository;

    @Transactional
    public String createApplication(HttpServletRequest request, @Valid ApplicationRequestDto applicationRequestDto) {
        Application application = Application.of(applicationRequestDto);
        application.setContact(memberService.removeHyphensFromContact(application.getContact()));
        application.setIsPass(false);
        application.setUpdateTime(LocalDateTime.now());
        String applierId = save(application).getStudentId();

        sendNotificationToAdminMembers(applicationRequestDto.getStudentId() + " " + applicationRequestDto.getName() + "님이 동아리에 지원하였습니다.");
        slackService.sendApplicationNotification(request, applicationRequestDto);

        return applierId;
    }

    public PagedResponseDto<ApplicationResponseDto> getApplications(Pageable pageable) {
        Page<Application> applications = getAllApplicationByOrderByCreatedAtDesc(pageable);
        return new PagedResponseDto<>(applications.map(ApplicationResponseDto::of));
    }

    public PagedResponseDto<ApplicationResponseDto> getApplicationsBetweenDates(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        Page<Application> applicationsBetweenDates = getApplicationByUpdateTimeBetween(pageable, startDateTime, endDateTime);
        return new PagedResponseDto<>(applicationsBetweenDates.map(ApplicationResponseDto::of));
    }

    public ApplicationResponseDto searchApplication(String applicationId) {
        try{
            Application application = getApplicationByIdOrThrow(applicationId);
            return ApplicationResponseDto.of(application);
        } catch (NotFoundException e) {
            throw e;
        }
    }

    public String changeApplicationApproval(String applicationId) {
        Application application = getApplicationByIdOrThrow(applicationId);
        application.setIsPass(!application.getIsPass());
        application.setUpdateTime(LocalDateTime.now());
        return save(application).getStudentId();
    }

    public PagedResponseDto<ApplicationResponseDto> getApprovedApplications(Pageable pageable) {
        Page<Application> applications = getApplicationByIsPass(pageable);
        if (applications.isEmpty()) {
            throw new NotFoundException("승인된 지원자가 없습니다.");
        }
        return new PagedResponseDto<>(applications.map(ApplicationResponseDto::of));
    }

    public ApplicationPassResponseDto getApplicationIsPass(String applicationId) {
        Application application = getApplicationById(applicationId);
        if (application == null) {
            return ApplicationPassResponseDto.builder()
                    .isPass(false)
                    .build();
        }
        return ApplicationPassResponseDto.of(application);
    }

    public String deleteApplication(String applicationId) {
        Application application = getApplicationByIdOrThrow(applicationId);
        delete(application);
        return application.getStudentId();
    }

    private void sendNotificationToAdminMembers(String content) {
        List<Member> admins = memberService.getMembersByRole(Role.SUPER);
        admins.addAll(memberService.getMembersByRole(Role.ADMIN));
        admins.stream()
            .forEach(admin -> {
                notificationService.createNotification(content, admin);
            });
    }

    private Application getApplicationByIdOrThrow(String applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NotFoundException("해당 지원자가 없습니다."));
    }

    private Application getApplicationById(String applicationId) {
        return applicationRepository.findById(applicationId)
                .orElse(null);
    }

    private Page<Application> getApplicationByUpdateTimeBetween(Pageable pageable, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return applicationRepository.findAllByUpdateTimeBetweenOrderByCreatedAtDesc(startDateTime, endDateTime, pageable);
    }

    private Page<Application> getApplicationByIsPass(Pageable pageable) {
        return applicationRepository.findAllByIsPassOrderByCreatedAtDesc(true, pageable);
    }

    private Page<Application> getAllApplicationByOrderByCreatedAtDesc(Pageable pageable) {
        return applicationRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    private Application save(Application application) {
        return applicationRepository.save(application);
    }

    private void delete(Application application) {
        applicationRepository.delete(application);
    }

}
