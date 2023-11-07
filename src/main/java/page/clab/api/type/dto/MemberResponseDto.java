package page.clab.api.type.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.type.entity.Member;
import page.clab.api.type.etc.MemberStatus;
import page.clab.api.type.etc.OAuthProvider;
import page.clab.api.type.etc.Role;
import page.clab.api.type.etc.StudentStatus;
import page.clab.api.util.ModelMapperUtil;

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

    private StudentStatus studentStatus;

    private String imageUrl;

    private MemberStatus memberStatus;

    private Role role;

    private OAuthProvider provider;

    private LocalDateTime createdAt;

    private LocalDateTime lastLoginTime;

    private LocalDateTime loanSuspensionDate;

    public static MemberResponseDto of(Member member) {
        return ModelMapperUtil.getModelMapper().map(member, MemberResponseDto.class);
    }

}
