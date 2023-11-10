package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.ApplicationRepository;
import page.clab.api.type.dto.ApplicationPassResponseDto;
import page.clab.api.type.dto.ApplicationRequestDto;
import page.clab.api.type.dto.ApplicationResponseDto;
import page.clab.api.type.entity.Application;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationService {

    private final MemberService memberService;

    private final ApplicationRepository applicationRepository;

    public void createApplication(ApplicationRequestDto appRequestDto) {
        Application application = Application.of(appRequestDto);
        application.setContact(memberService.removeHyphensFromContact(application.getContact()));
        application.setIsPass(false);
        applicationRepository.save(application);
    }

    public List<ApplicationResponseDto> getApplications() throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        List<Application> applications = applicationRepository.findAll();
        return applications.stream()
                .map(ApplicationResponseDto::of)
                .collect(Collectors.toList());
    }

    public List<ApplicationResponseDto> getApplicationsBetweenDates(LocalDate startDate, LocalDate endDate) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        List<Application> applicationsBetweenDates = applicationRepository.findApplicationsBetweenDates(startDateTime, endDateTime);
        return applicationsBetweenDates.stream()
                .map(ApplicationResponseDto::of)
                .collect(Collectors.toList());
    }

    public ApplicationResponseDto searchApplication(String applicationId) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        Application application = null;
        if (applicationId != null) {
            application = getApplicationByIdOrThrow(applicationId);
        }
        return ApplicationResponseDto.of(application);
    }

    @Transactional
    public void approveApplication(String applicationId) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        Application application = getApplicationByIdOrThrow(applicationId);
        if (application.getIsPass()) {
            application.setIsPass(false);
            application.setUpdateTime(LocalDateTime.now());
            applicationRepository.save(application);
        } else {
            application.setIsPass(true);
            application.setUpdateTime(LocalDateTime.now());
            applicationRepository.save(application);
        }
    }

    @Transactional
    public List<ApplicationResponseDto> getApprovedApplications() throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        List<Application> applications = applicationRepository.findAllByIsPass(true);
        if (applications.isEmpty()) {
            throw new NotFoundException("승인된 신청자가 없습니다.");
        } else {
            return applications.stream()
                    .map(ApplicationResponseDto::of)
                    .collect(Collectors.toList());
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

    public void deleteApplication(String applicationId) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        Application application = getApplicationByIdOrThrow(applicationId);
        applicationRepository.delete(application);
    }

    private Application getApplicationByIdOrThrow(String applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NotFoundException("해당 신청자가 없습니다."));
    }

    private Application getApplicationById(String applicationId) {
        return applicationRepository.findById(applicationId)
                .orElse(null);
    }

}
