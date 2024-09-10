package page.clab.api.domain.hiring.application.domain;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Application {

    private String studentId;
    private Long recruitmentId;
    private String name;
    private String contact;
    private String email;
    private String department;
    private Long grade;
    private LocalDate birth;
    private String address;
    private String interests;

    @Size(min = 1, max = 1000, message = "{size.application.otherActivities}")
    @Column(nullable = false)
    private String otherActivities;

    private String githubUrl;
    private ApplicationType applicationType;
    private Boolean isPass;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void approve() {
        this.isPass = true;
    }

    public void reject() {
        this.isPass = false;
    }

    public String getApplicationTypeForNotificationPrefix() {
        return "[" + this.applicationType.getDescription() + "] ";
    }
}
