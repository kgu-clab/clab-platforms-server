package page.clab.api.domain.hiring.application.application.dto.request;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApplicationMemberCreationDto {

    private String studentId;
    private String name;
    private String contact;
    private String email;
    private String department;
    private Long grade;
    private LocalDate birth;
    private String address;
    private String interests;
    private String githubUrl;
}
