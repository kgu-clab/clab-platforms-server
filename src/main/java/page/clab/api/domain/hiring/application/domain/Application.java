package page.clab.api.domain.hiring.application.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
