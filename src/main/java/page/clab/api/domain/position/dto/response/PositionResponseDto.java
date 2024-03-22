package page.clab.api.domain.position.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.position.domain.Position;
import page.clab.api.domain.position.domain.PositionType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    public static PositionResponseDto toDto(Position position) {
        Member member = position.getMember();
        return PositionResponseDto.builder()
                .id(position.getId())
                .name(member.getName())
                .email(member.getEmail())
                .imageUrl(member.getImageUrl())
                .interests(member.getInterests())
                .githubUrl(member.getGithubUrl())
                .positionType(position.getPositionType())
                .year(position.getYear())
                .build();
    }

}
