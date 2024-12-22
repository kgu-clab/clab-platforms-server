package page.clab.api.domain.memberManagement.member.application.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.memberManagement.member.domain.StudentStatus;

@Getter
@Builder
public class MyProfileResponseDto {

    private String name;
    private String id;
    private String interests;
    private String contact;
    private String email;
    private String address;
    private String githubUrl;
    private StudentStatus studentStatus;
    private String imageUrl;
    private Long roleLevel;
    private Boolean isOtpEnabled;
    private LocalDateTime createdAt;
}
