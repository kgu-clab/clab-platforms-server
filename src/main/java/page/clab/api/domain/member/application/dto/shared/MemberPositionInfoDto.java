package page.clab.api.domain.member.application.dto.shared;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.member.domain.Member;

@Getter
@Builder
public class MemberPositionInfoDto {

    private String memberId;

    private String memberName;

    private String email;

    private String imageUrl;

    private String interests;

    private String githubUrl;

    public static MemberPositionInfoDto create(Member member) {
        return MemberPositionInfoDto.builder()
                .memberId(member.getId())
                .memberName(member.getName())
                .email(member.getEmail())
                .imageUrl(member.getImageUrl())
                .interests(member.getInterests())
                .githubUrl(member.getGithubUrl())
                .build();
    }

}
