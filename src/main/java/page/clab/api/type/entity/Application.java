package page.clab.api.type.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import page.clab.api.type.dto.ApplicationRequestDto;
import page.clab.api.type.dto.ApplicationResponseDto;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Application {

    @Id
    @Size(min = 9, max = 9)
    private String studentId;

    @Column(nullable = false)
    @Size(max = 10)
    private String name;

    @Column(nullable = false)
    private String contact;

    @Column(nullable = false)
    @Email
    private String email;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private Long grade;

    @Column(nullable = false)
    @DateTimeFormat
    private LocalDate birth;

    @Column(nullable = false)
    private String address;

    private String interests;

    private String otherActivities;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // ApplicationService로 빠질 예정
    public static Application toApplication(ApplicationRequestDto applicationRequestDto) {
        Application application = Application.builder()
                .studentId(applicationRequestDto.getStudentId())
                .name(applicationRequestDto.getName())
                .contact(applicationRequestDto.getContact())
                .email(applicationRequestDto.getEmail())
                .department(applicationRequestDto.getDepartment())
                .grade(applicationRequestDto.getGrade())
                .birth(applicationRequestDto.getBirth())
                .address(applicationRequestDto.getAddress())
                .interests(applicationRequestDto.getInterests())
                .otherActivities(applicationRequestDto.getOtherActivities())
                .build();
        return application;
    }

    public static ApplicationResponseDto toApplicationResponseDto(Application application) {
        ApplicationResponseDto applicationResponseDto = ApplicationResponseDto.builder()
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
        return applicationResponseDto;
    }

}