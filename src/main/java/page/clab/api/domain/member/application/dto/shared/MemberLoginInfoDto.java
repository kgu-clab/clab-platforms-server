package page.clab.api.domain.member.application.dto.shared;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.member.domain.Role;

@Getter
@Builder
public class MemberLoginInfoDto {

    private String memberId;

    private String memberName;

    private Role role;

    private boolean isOtpEnabled;

    public static MemberLoginInfoDto create(Member member) {
        return MemberLoginInfoDto.builder()
                .memberId(member.getId())
                .memberName(member.getName())
                .role(member.getRole())
                .isOtpEnabled(member.getIsOtpEnabled())
                .build();
    }

    public boolean isAdminRole() {
        return role.toRoleLevel() >= 2;
    }

    public boolean isSuperAdminRole() {
        return role.toRoleLevel() == 3;
    }

}
