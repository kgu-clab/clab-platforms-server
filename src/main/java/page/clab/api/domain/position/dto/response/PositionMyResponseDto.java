package page.clab.api.domain.position.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.position.domain.Position;
import page.clab.api.domain.position.domain.PositionType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PositionMyResponseDto {

    private Long id;

    private String name;

    private String email;

    private String imageUrl;

    private String interests;

    private String githubUrl;

    private Map<String, List<PositionType>> positionTypes;

    public static PositionMyResponseDto toDto(List<Position> positions) {
        Member member = positions.getFirst().getMember();
        Map<String, List<PositionType>> positionTypesByYear = positions.stream()
                .collect(Collectors.groupingBy(
                        Position::getYear,
                        Collectors.mapping(Position::getPositionType, Collectors.toList())
                ));
        return PositionMyResponseDto.builder()
                .name(member.getName())
                .email(member.getEmail())
                .imageUrl(member.getImageUrl())
                .interests(member.getInterests())
                .githubUrl(member.getGithubUrl())
                .positionTypes(positionTypesByYear)
                .build();
    }

}
