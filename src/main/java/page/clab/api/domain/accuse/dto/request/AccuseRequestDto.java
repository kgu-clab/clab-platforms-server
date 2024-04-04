package page.clab.api.domain.accuse.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.accuse.domain.Accuse;
import page.clab.api.domain.accuse.domain.AccuseStatus;
import page.clab.api.domain.accuse.domain.TargetType;
import page.clab.api.domain.member.domain.Member;

@Getter
@Setter
public class AccuseRequestDto {

    @NotNull(message = "{notNull.accuse.targetType}")
    @Schema(description = "신고 대상 유형", example = "BOARD", required = true)
    private TargetType targetType;

    @NotNull(message = "{notNull.accuse.targetId}")
    @Schema(description = "신고 대상 ID", example = "1", required = true)
    private Long targetId;

    @NotNull(message = "{notNull.accuse.reason}")
    @Schema(description = "신고 사유", example = "부적절한 게시글입니다.", required = true)
    private String reason;

    public static Accuse toEntity(AccuseRequestDto requestDto, Member member) {
        return Accuse.builder()
                .id(null)
                .member(member)
                .targetType(requestDto.getTargetType())
                .targetId(requestDto.getTargetId())
                .reason(requestDto.getReason())
                .accuseStatus(AccuseStatus.PENDING)
                .build();
    }

}
