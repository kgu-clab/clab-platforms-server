package page.clab.api.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.member.domain.Member;

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

    private String imageUrl;

    private Long roleLevel;

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
                .imageUrl(member.getImageUrl())
                .roleLevel(member.getRole().toRoleLevel())
                .createdAt(member.getCreatedAt())
                .build();
    }

}
