package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.auth.exception.UnAuthorizeException;
import page.clab.api.exception.NotFoundException;
import page.clab.api.repository.ApplicationRepository;
import page.clab.api.repository.UserRepository;
import page.clab.api.type.dto.ApplicationRequestDto;
import page.clab.api.type.dto.ApplicationResponseDto;
import page.clab.api.type.entity.Application;
import page.clab.api.type.entity.User;
import page.clab.api.type.etc.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    private final UserRepository userRepository;

    public void createApplication(ApplicationRequestDto applicationRequestDto) {
        Application application = Application.toApplication(applicationRequestDto);
        applicationRepository.save(application);
    }

    public List<ApplicationResponseDto> getAllApplication(String userId) {
        User user = userRepository.getUserById(userId).orElseThrow(() -> new NotFoundException("해당 유저가 없습니다."));
        if (!user.getRole().equals(Role.ADMIN.getKey()))
            new UnAuthorizeException("권한이 부족합니다.");
        List<Application> applications = applicationRepository.findAll();
        List<ApplicationResponseDto> applicationResponseDtos = new ArrayList<>();
        for (Application a : applications) {
            ApplicationResponseDto applicationResponseDto = Application.toApplicationResponseDto(a);
            if (userRepository.findById(a.getStudentId()).isPresent())
                applicationResponseDto.setPass(true);
            applicationResponseDtos.add(applicationResponseDto);
        }
        return applicationResponseDtos;
    }

    public List<ApplicationResponseDto> getApplicationsBetweenDates(LocalDate startDate, LocalDate endDate, String userId) {
        User user = userRepository.getUserById(userId).orElseThrow(() -> new NotFoundException("해당 유저가 없습니다."));
        if (!user.getRole().equals(Role.ADMIN.getKey()))
            new UnAuthorizeException("권한이 부족합니다.");
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        List<Application> applicationsBetweenDates = applicationRepository.findApplicationsBetweenDates(startDateTime, endDateTime);
        List<ApplicationResponseDto> applicationResponseDtos = new ArrayList<>();
        for (Application a : applicationsBetweenDates) {
            ApplicationResponseDto applicationResponseDto = Application.toApplicationResponseDto(a);
            if (userRepository.findById(a.getStudentId()).isPresent())
                applicationResponseDto.setPass(true);
            applicationResponseDtos.add(applicationResponseDto);
        }
        return applicationResponseDtos;
    }

    public ApplicationResponseDto getApplicationById(String applicationId, String userId) {
        User user = userRepository.getUserById(userId).orElseThrow(() -> new NotFoundException("해당 유저가 없습니다."));
        if (!user.getRole().equals(Role.ADMIN.getKey()))
            new UnAuthorizeException("권한이 부족합니다.");
        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new NotFoundException("해당 신청자가 없습니다."));
        ApplicationResponseDto applicationResponseDto = Application.toApplicationResponseDto(application);
        if (userRepository.findById(applicationResponseDto.getStudentId()).isPresent())
            applicationResponseDto.setPass(true);
        return applicationResponseDto;
    }

    public List<ApplicationResponseDto> getApprovedApplications(String userId) {
        User user = userRepository.getUserById(userId).orElseThrow(() -> new NotFoundException("해당 유저가 없습니다."));
        if (!user.getRole().equals(Role.ADMIN.getKey()))
            new UnAuthorizeException("권한이 부족합니다.");
        List<Application> applications = applicationRepository.findAll();
        List<ApplicationResponseDto> applicationResponseDtos = new ArrayList<>();
        for (Application a : applications) {
            Application application = applicationRepository.findById(a.getStudentId()).orElseThrow(() -> new NotFoundException("해당 신청자가 없습니다."));
            ApplicationResponseDto applicationResponseDto = Application.toApplicationResponseDto(a);
            if (userRepository.findById(applicationResponseDto.getStudentId()).isPresent()) {
                applicationResponseDto.setPass(true);
                applicationResponseDtos.add(applicationResponseDto);
            }
        }
        return applicationResponseDtos;
    }

    public ApplicationResponseDto searchApplication(String name, String userId) {
        User user = userRepository.getUserById(userId).orElseThrow(() -> new NotFoundException("해당 유저가 없습니다."));
        if (!user.getRole().equals(Role.ADMIN.getKey()))
            new UnAuthorizeException("권한이 부족합니다.");
        Application application = applicationRepository.findByName(name).orElseThrow(() -> new NotFoundException("해당 신청자가 없습니다."));
        ApplicationResponseDto applicationResponseDto = Application.toApplicationResponseDto(application);
        if (userRepository.findById(applicationResponseDto.getStudentId()).isPresent())
            applicationResponseDto.setPass(true);
        return applicationResponseDto;
    }

    public void approveApplication(String applicationId, String userId) {
        User user = userRepository.getUserById(userId).orElseThrow(() -> new NotFoundException("해당 유저가 없습니다."));
        if (!user.getRole().equals(Role.ADMIN.getKey()))
            new UnAuthorizeException("권한이 부족합니다.");
        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new NotFoundException("해당 신청자가 없습니다."));
        User approvedUser = User.toUser(application);
        userRepository.save(approvedUser);
    }

    public void cancelApplication(String applicationId, String userId) {
        User user = userRepository.getUserById(userId).orElseThrow(() -> new NotFoundException("해당 유저가 없습니다."));
        if (!user.getRole().equals(Role.ADMIN.getKey()))
            new UnAuthorizeException("권한이 부족합니다.");
        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new NotFoundException("해당 신청자가 없습니다."));
        User approvedUser = userRepository.findById(applicationId).orElseThrow(() -> new NotFoundException("해당 유저가 없습니다."));
        if (approvedUser.getCreatedAt().isBefore(LocalDateTime.now().minusDays(1))) {
            throw new UnAuthorizeException("취소할 수 없는 신청입니다.");
        }
        userRepository.delete(approvedUser);
    }
}
