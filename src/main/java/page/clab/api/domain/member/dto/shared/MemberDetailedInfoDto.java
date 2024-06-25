package page.clab.api.domain.member.dto.shared;

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

    public static MemberDetailedInfoDto create(Member member) {
        return MemberDetailedInfoDto.builder()
                .memberId(member.getId())
                .memberName(member.getName())
                .roleLevel(member.getRole().toRoleLevel())
                .imageUrl(member.getImageUrl())
                .build();
    }

}
