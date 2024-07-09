package page.clab.api.domain.member.application.dto.shared;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.member.domain.Member;

@Getter
@Builder
public class MemberBasicInfoDto {

    private String memberId;

    private String memberName;

    public static MemberBasicInfoDto create(Member member) {
        return MemberBasicInfoDto.builder()
                .memberId(member.getId())
                .memberName(member.getName())
                .build();
    }

}
