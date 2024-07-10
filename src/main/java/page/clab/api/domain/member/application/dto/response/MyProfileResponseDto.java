package page.clab.api.domain.member.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.domain.StudentStatus;

import java.time.LocalDateTime;

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

    public static MyProfileResponseDto toDto(Member member) {
        return MyProfileResponseDto.builder()
                .name(member.getName())
                .id(member.getId())
                .interests(member.getInterests())
                .contact(member.getContact())
                .email(member.getEmail())
                .address(member.getAddress())
                .githubUrl(member.getGithubUrl())
                .studentStatus(member.getStudentStatus())
                .imageUrl(member.getImageUrl())
                .roleLevel(member.getRole().toRoleLevel())
                .isOtpEnabled(member.getIsOtpEnabled())
                .createdAt(member.getCreatedAt())
                .build();
    }

}
