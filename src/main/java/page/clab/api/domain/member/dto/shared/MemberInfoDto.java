package page.clab.api.domain.member.dto.shared;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.member.domain.Member;

@Getter
@Builder
public class MemberInfoDto {

    private String memberId;

    private String memberName;

    public static MemberInfoDto create(Member member) {
        return MemberInfoDto.builder()
                .memberId(member.getId())
                .memberName(member.getName())
                .build();
    }

}
