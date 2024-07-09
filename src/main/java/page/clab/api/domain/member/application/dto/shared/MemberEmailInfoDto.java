package page.clab.api.domain.member.application.dto.shared;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.member.domain.Member;

@Getter
@Builder
public class MemberEmailInfoDto {

    private String memberName;

    private String email;

    public static MemberEmailInfoDto create(Member member) {
        return MemberEmailInfoDto.builder()
                .memberName(member.getName())
                .email(member.getEmail())
                .build();
    }

}
