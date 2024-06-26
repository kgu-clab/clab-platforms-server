package page.clab.api.domain.position.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.member.dto.shared.MemberPositionInfoDto;
import page.clab.api.domain.position.domain.Position;
import page.clab.api.domain.position.domain.PositionType;

@Getter
@Builder
public class PositionResponseDto {

    private Long id;

    private String name;

    private String email;

    private String imageUrl;

    private String interests;

    private String githubUrl;

    private PositionType positionType;

    private String year;

    public static PositionResponseDto toDto(Position position, MemberPositionInfoDto memberInfo) {
        return PositionResponseDto.builder()
                .id(position.getId())
                .name(memberInfo.getMemberName())
                .email(memberInfo.getEmail())
                .imageUrl(memberInfo.getImageUrl())
                .interests(memberInfo.getInterests())
                .githubUrl(memberInfo.getGithubUrl())
                .positionType(position.getPositionType())
                .year(position.getYear())
                .build();
    }

}
