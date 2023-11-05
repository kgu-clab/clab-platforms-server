package page.clab.api.type.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import page.clab.api.type.dto.ApplicationRequestDto;
import page.clab.api.type.etc.ApplicationType;
import page.clab.api.util.ModelMapperUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Application {

    @Id
    private String studentId;

    @Column(nullable = false)
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
    private LocalDate birth;

    @Column(nullable = false)
    private String address;

    private String interests;

    private String otherActivities;

    private String githubUrl;

    @Enumerated(EnumType.STRING)
    private ApplicationType applicationType;

    private Boolean isPass;

    @CreationTimestamp
    private LocalDateTime updateTime;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public static Application of(ApplicationRequestDto applicationRequestDto) {
        return ModelMapperUtil.getModelMapper().map(applicationRequestDto, Application.class);
    }

}