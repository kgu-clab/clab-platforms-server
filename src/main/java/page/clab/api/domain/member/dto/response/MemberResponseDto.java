package page.clab.api.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.domain.Role;
import page.clab.api.domain.member.domain.StudentStatus;
import page.clab.api.global.util.ModelMapperUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberResponseDto {

    private String id;

    private String name;

    private String contact;

    private String email;

    private String department;

    private Long grade;

    private LocalDate birth;

    private String address;

    private String interests;

    private String githubUrl;

    private StudentStatus studentStatus;

    private String imageUrl;

    private Role role;

    private LocalDateTime createdAt;

    private LocalDateTime lastLoginTime;

    private LocalDateTime loanSuspensionDate;

    public static MemberResponseDto of(Member member) {
        return ModelMapperUtil.getModelMapper().map(member, MemberResponseDto.class);
    }

}
