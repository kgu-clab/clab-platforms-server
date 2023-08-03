package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import page.clab.api.auth.exception.UnAuthorizeException;
import page.clab.api.exception.AlreadyApprovedException;
import page.clab.api.exception.NotFoundException;
import page.clab.api.exception.PermissionDeniedException;
import page.clab.api.exception.SearchResultNotExistException;
import page.clab.api.repository.ApplicationRepository;
import page.clab.api.repository.UserRepository;
import page.clab.api.type.dto.ApplicationRequestDto;
import page.clab.api.type.dto.ApplicationResponseDto;
import page.clab.api.type.entity.Application;
import page.clab.api.type.entity.User;
import page.clab.api.type.etc.OAuthProvider;
import page.clab.api.type.etc.Role;

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

    private final ApplicationRepository applicationRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    public void createApplication(ApplicationRequestDto appRequestDto) {
        Application application = toApplication(appRequestDto);
        applicationRepository.save(application);
    }

    public List<ApplicationResponseDto> getApplications() throws PermissionDeniedException {
        checkUserAdminRole();
        List<Application> applications = applicationRepository.findAll();
        List<ApplicationResponseDto> appRequestDtos = new ArrayList<>();
        for (Application application : applications) {
            ApplicationResponseDto appRequestDto = createApplicationResponseDto(application);
            appRequestDtos.add(appRequestDto);
        }
        return appRequestDtos;
    }

    public List<ApplicationResponseDto> getApplicationsBetweenDates(LocalDate startDate, LocalDate endDate) throws PermissionDeniedException {
        checkUserAdminRole();
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        List<Application> applicationsBetweenDates = applicationRepository.findApplicationsBetweenDates(startDateTime, endDateTime);
        List<ApplicationResponseDto> appRequestDtos = new ArrayList<>();
        for (Application application : applicationsBetweenDates) {
            ApplicationResponseDto appRequestDto = createApplicationResponseDto(application);
            appRequestDtos.add(appRequestDto);
        }
        return appRequestDtos;
    }

    @Transactional
    public List<ApplicationResponseDto> getApprovedApplications() throws PermissionDeniedException {
        checkUserAdminRole();
        List<Application> applications = applicationRepository.findAll();
        return applications.stream()
                .map(this::createApplicationResponseDto)
                .filter(ApplicationResponseDto::isPass)
                .collect(Collectors.toList());
    }

    public ApplicationResponseDto searchApplication(String applicationId, String name) throws PermissionDeniedException {
        ApplicationResponseDto appResponseDto = null;
        if (applicationId != null)
            appResponseDto = searchApplicationById(applicationId);
        else if (name != null)
            appResponseDto = searchApplicationByName(name);
        else
            throw new IllegalArgumentException("적어도 applicationId 또는 name 중 하나를 제공해야 합니다.");

        if (appResponseDto == null)
            throw new SearchResultNotExistException("검색 결과가 존재하지 않습니다.");
        return appResponseDto;
    }

    public ApplicationResponseDto searchApplicationById(String applicationId) throws PermissionDeniedException {
        checkUserAdminRole();
        Application application = getApplicationByIdOrThrow(applicationId);
        ApplicationResponseDto appRequestDto = createApplicationResponseDto(application);
        if (appRequestDto == null)
            throw new SearchResultNotExistException("검색 결과가 존재하지 않습니다.");
        return appRequestDto;
    }

    public ApplicationResponseDto searchApplicationByName(String name) throws PermissionDeniedException {
        checkUserAdminRole();
        Application application = getApplicationByNameOrThrow(name);
        ApplicationResponseDto appRequestDto = createApplicationResponseDto(application);
        if (appRequestDto == null)
            throw new SearchResultNotExistException("검색 결과가 존재하지 않습니다.");
        return appRequestDto;
    }

    @Transactional
    public void approveApplication(String applicationId) throws PermissionDeniedException {
        checkUserAdminRole();
        if (userRepository.existsById(applicationId))
            throw new AlreadyApprovedException("이미 승인된 신청자입니다.");
        Application application = getApplicationByIdOrThrow(applicationId);
        User approvedUser = toUser(application);
        userRepository.save(approvedUser);
    }

    @Transactional
    public void cancelApplication(String applicationId) throws PermissionDeniedException {
        checkUserAdminRole();
        Application application = getApplicationByIdOrThrow(applicationId);
        User approvedUser = userService.getUserByIdOrThrow(applicationId);
        if (approvedUser.getCreatedAt().isBefore(LocalDateTime.now().minusDays(1)))
            throw new UnAuthorizeException("취소할 수 없는 신청입니다.");
        userRepository.delete(approvedUser);
    }

    public void deleteApplication(String applicationId) throws PermissionDeniedException {
        checkUserAdminRole();
        Application application = getApplicationByIdOrThrow(applicationId);
        applicationRepository.delete(application);
    }

    private void checkUserAdminRole() throws PermissionDeniedException {
//        User user = AuthUtil.getAuthenticationInfo();
        User user = userRepository.findById("201912156").get(); // 임시 테스트용 | 로그인 구현 후 삭제할 것
        if (!user.getRole().equals(Role.ADMIN)) {
            throw new PermissionDeniedException("권한이 부족합니다.");
        }
    }

    public Application getApplicationByIdOrThrow(String applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NotFoundException("해당 신청자가 없습니다."));
    }

    public Application getApplicationByNameOrThrow(String name) {
        return applicationRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("해당 신청자가 없습니다."));
    }

    private ApplicationResponseDto createApplicationResponseDto(Application application) {
        ApplicationResponseDto appRequestDto = toApplicationResponseDto(application);
        if (userRepository.findById(application.getStudentId()).isPresent())
            appRequestDto.setPass(true);
        return appRequestDto;
    }

    private Application toApplication(ApplicationRequestDto appRequestDto) {
        Application application = Application.builder()
                .studentId(appRequestDto.getStudentId())
                .name(appRequestDto.getName())
                .contact(appRequestDto.getContact())
                .email(appRequestDto.getEmail())
                .department(appRequestDto.getDepartment())
                .grade(appRequestDto.getGrade())
                .birth(appRequestDto.getBirth())
                .address(appRequestDto.getAddress())
                .interests(appRequestDto.getInterests())
                .otherActivities(appRequestDto.getOtherActivities())
                .build();
        return application;
    }

    private ApplicationResponseDto toApplicationResponseDto(Application application) {
        ApplicationResponseDto appRequestDto = ApplicationResponseDto.builder()
                .studentId(application.getStudentId())
                .name(application.getName())
                .contact(application.getContact())
                .email(application.getEmail())
                .department(application.getDepartment())
                .grade(application.getGrade())
                .birth(application.getBirth())
                .address(application.getAddress())
                .interests(application.getInterests())
                .otherActivities(application.getOtherActivities())
                .isPass(false)
                .createdAt(application.getCreatedAt())
                .build();
        return appRequestDto;
    }

    private User toUser(Application application) {
        User user = User.builder()
                .id(application.getStudentId())
                .password(userService.generatePassword(application.getBirth()))
                .name(application.getName())
                .contact(application.getContact())
                .email(application.getEmail())
                .department(application.getDepartment())
                .grade(application.getGrade())
                .birth(application.getBirth())
                .address(application.getAddress())
                .isInSchool(true)
                .role(Role.USER)
                .provider(OAuthProvider.LOCAL)
                .build();
        return user;
    }

}
