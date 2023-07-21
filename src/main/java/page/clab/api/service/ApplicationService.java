package page.clab.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import page.clab.api.auth.exception.UnAuthorizeException;
import page.clab.api.exception.NotFoundException;
import page.clab.api.repository.ApplicationRepository;
import page.clab.api.repository.UserRepository;
import page.clab.api.type.dto.ApplicationRequestDto;
import page.clab.api.type.entity.Application;
import page.clab.api.type.entity.User;
import page.clab.api.type.etc.Role;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    private final UserRepository userRepository;

    public void createApplication(ApplicationRequestDto applicationRequestDto) {
        Application application = Application.builder()
                .studentId(applicationRequestDto.getStudentId())
                .name(applicationRequestDto.getName())
                .contact(applicationRequestDto.getContact())
                .email(applicationRequestDto.getEmail())
                .department(applicationRequestDto.getDepartment())
                .grade(applicationRequestDto.getGrade())
                .address(applicationRequestDto.getAddress())
                .interests(applicationRequestDto.getInterests())
                .otherActivities(applicationRequestDto.getOtherActivities())
                .build();
        applicationRepository.save(application);
    }

    public List<Application> getAllApplication(String userId) {
        User user = userRepository.getUserById(userId).orElseThrow(() -> new NotFoundException("해당 유저가 없습니다."));
        if (!user.getRole().equals(Role.ADMIN.getKey()))
            new UnAuthorizeException("권한이 부족합니다.");
        List<Application> applications = applicationRepository.findAll();
        return applications;
    }

    public Application getApplication(String applicationId, String userId) {
        User user = userRepository.getUserById(userId).orElseThrow(() -> new NotFoundException("해당 유저가 없습니다."));
        if (!user.getRole().equals(Role.ADMIN.getKey()))
            new UnAuthorizeException("권한이 부족합니다.");
        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new NotFoundException("해당 신청자가 없습니다."));
        return application;
    }
}
