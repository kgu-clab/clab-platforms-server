package page.clab.api.domain.position.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.member.application.dto.shared.MemberPositionInfoDto;
import page.clab.api.domain.position.domain.Position;
import page.clab.api.domain.position.domain.PositionType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
public class PositionMyResponseDto {

    private Long id;
    private String name;
    private String email;
    private String imageUrl;
    private String interests;
    private String githubUrl;
    private Map<String, List<PositionType>> positionTypes;

    public static PositionMyResponseDto toDto(List<Position> positions, MemberPositionInfoDto memberInfo) {
        Map<String, List<PositionType>> positionTypesByYear = positions.stream()
                .collect(Collectors.groupingBy(
                        Position::getYear,
                        Collectors.mapping(Position::getPositionType, Collectors.toList())
                ));
        return PositionMyResponseDto.builder()
                .name(memberInfo.getMemberName())
                .email(memberInfo.getEmail())
                .imageUrl(memberInfo.getImageUrl())
                .interests(memberInfo.getInterests())
                .githubUrl(memberInfo.getGithubUrl())
                .positionTypes(positionTypesByYear)
                .build();
    }
}
