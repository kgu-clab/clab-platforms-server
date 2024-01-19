package page.clab.api.domain.application.application;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import page.clab.api.global.exception.NotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationService {

    private final MemberService memberService;

    private final NotificationService notificationService;

    private final ApplicationRepository applicationRepository;

    @Transactional
    public String createApplication(@Valid ApplicationRequestDto applicationRequestDto) {
        Application application = Application.of(applicationRequestDto);
        application.setContact(memberService.removeHyphensFromContact(application.getContact()));
        application.setIsPass(false);
        application.setUpdateTime(LocalDateTime.now());
        String id = applicationRepository.save(application).getStudentId();
        List<Member> admins = memberService.getMembersByRole(Role.SUPER);
        admins.addAll(memberService.getMembersByRole(Role.ADMIN));
        admins.stream()
                .forEach(admin -> {
                    NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                            .memberId(admin.getId())
                            .content(applicationRequestDto.getStudentId() + " " + applicationRequestDto.getName() + "님이 동아리 가입을 신청하였습니다.")
                            .build();
                    notificationService.createNotification(notificationRequestDto);
                });
        return id;
    }

    public PagedResponseDto<ApplicationResponseDto> getApplications(Pageable pageable) {
        Page<Application> applications = applicationRepository.findAllByOrderByCreatedAtDesc(pageable);
        return new PagedResponseDto<>(applications.map(ApplicationResponseDto::of));
    }

    public PagedResponseDto<ApplicationResponseDto> getApplicationsBetweenDates(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        Page<Application> applicationsBetweenDates = getApplicationByUpdateTimeBetween(pageable, startDateTime, endDateTime);
        return new PagedResponseDto<>(applicationsBetweenDates.map(ApplicationResponseDto::of));
    }

    public ApplicationResponseDto searchApplication(String applicationId) {
        Application application = null;
        if (applicationId != null) {
            application = getApplicationByIdOrThrow(applicationId);
        }
        return ApplicationResponseDto.of(application);
    }

    public String approveApplication(String applicationId) {
        Application application = getApplicationByIdOrThrow(applicationId);
        application.setIsPass(!application.getIsPass());
        application.setUpdateTime(LocalDateTime.now());
        return applicationRepository.save(application).getStudentId();
    }

    public PagedResponseDto<ApplicationResponseDto> getApprovedApplications(Pageable pageable) {
        Page<Application> applications = getApplicationByIsPass(pageable);
        if (applications.isEmpty()) {
            throw new NotFoundException("승인된 신청자가 없습니다.");
        } else {
            return new PagedResponseDto<>(applications.map(ApplicationResponseDto::of));
        }
    }

    public ApplicationPassResponseDto getApplicationPass(String applicationId) {
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
        applicationRepository.delete(application);
        return application.getStudentId();
    }

    private Application getApplicationByIdOrThrow(String applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NotFoundException("해당 신청자가 없습니다."));
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

}
