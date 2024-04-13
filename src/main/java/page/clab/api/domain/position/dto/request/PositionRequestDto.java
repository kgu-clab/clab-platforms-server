package page.clab.api.domain.position.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.member.domain.Member;
import page.clab.api.domain.position.domain.Position;
import page.clab.api.domain.position.domain.PositionType;

@Getter
@Setter
public class PositionRequestDto {

    @NotNull(message = "{notNull.position.memberId}")
    @Schema(description = "학번", example = "201912156", required = true)
    private String memberId;

    @NotNull(message = "{notNull.position.position}")
    @Schema(description = "직책", example = "OPERATION", required = true)
    private PositionType positionType;

    @NotNull(message = "{notNull.position.year}")
    @Schema(description = "연도", example = "2023", required = true)
    private String year;

    public static Position toEntity(PositionRequestDto positionRequestDto) {
        return Position.builder()
                .member(Member.builder().id(positionRequestDto.getMemberId()).build())
                .positionType(positionRequestDto.getPositionType())
                .year(positionRequestDto.getYear())
                .build();
    }

}
