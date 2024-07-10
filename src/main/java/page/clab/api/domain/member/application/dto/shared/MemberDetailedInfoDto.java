package page.clab.api.domain.member.application.dto.shared;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.member.domain.Member;

@Getter
@Builder
public class MemberDetailedInfoDto {

    private String memberId;
    private String memberName;
    private Long roleLevel;
    private String imageUrl;
    private boolean isGraduated;

    public static MemberDetailedInfoDto create(Member member) {
        return MemberDetailedInfoDto.builder()
                .memberId(member.getId())
                .memberName(member.getName())
                .roleLevel(member.getRole().toRoleLevel())
                .imageUrl(member.getImageUrl())
                .isGraduated(member.isGraduated())
                .build();
    }

    public boolean isAdminRole() {
        return roleLevel >= 2;
    }

    public boolean isSuperAdminRole() {
        return roleLevel == 3;
    }
}
