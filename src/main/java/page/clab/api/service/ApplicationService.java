package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.repository.ApplicationRepository;
import page.clab.api.type.dto.ApplicationRequestDto;
import page.clab.api.type.dto.ApplicationResponseDto;
import page.clab.api.type.entity.Application;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public List<ApplicationResponseDto> searchApplication(String applicationId, String name) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        List<Application> applications = new ArrayList<>();
        if (applicationId != null) {
            applications.add(getApplicationByIdOrThrow(applicationId));
        } else if (name != null) {
            applications.addAll(getApplicationByName(name));
        } else {
            throw new IllegalArgumentException("적어도 applicationId, name 중 하나를 제공해야 합니다.");
        }
        return applications.stream()
                .map(ApplicationResponseDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void approveApplication(String applicationId) throws PermissionDeniedException {
        memberService.checkMemberAdminRole();
        Application application = getApplicationByIdOrThrow(applicationId);
        if (application.getIsPass()) {
            application.setIsPass(false);
            applicationRepository.save(application);
        } else {
            application.setIsPass(true);
            applicationRepository.save(application);
        }
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

    private List<Application> getApplicationByName(String name) {
        return applicationRepository.findAllByName(name);
    }

}
