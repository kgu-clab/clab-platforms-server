package page.clab.api.domain.position.dto.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class PositionMyResponseDto {

    private Long id;

    private String name;

    private String email;

    private String imageUrl;

    private String interests;

    private String githubUrl;

    private Map<String, List<PositionType>> positionTypes;

    public static PositionMyResponseDto of(List<Position> positions) {
        Member member = positions.getFirst().getMember();
        PositionMyResponseDto positionResponseDto = PositionMyResponseDto.builder()
                .name(member.getName())
                .email(member.getEmail())
                .imageUrl(member.getImageUrl())
                .interests(member.getInterests())
                .githubUrl(member.getGithubUrl())
                .positionTypes(new HashMap<>())
                .build();
        positions.forEach(position -> {
            String year = position.getYear();
            PositionType positionType = position.getPositionType();
            if (positionResponseDto.getPositionTypes().containsKey(year)) {
                positionResponseDto.getPositionTypes().get(year).add(positionType);
            } else {
                List<PositionType> positionTypes = new ArrayList<>();
                positionTypes.add(positionType);
                positionResponseDto.getPositionTypes().put(year, positionTypes);
            }
        });
        return positionResponseDto;
    }

}
