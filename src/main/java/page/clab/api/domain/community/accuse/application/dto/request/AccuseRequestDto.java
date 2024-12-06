package page.clab.api.domain.community.accuse.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import page.clab.api.domain.community.accuse.domain.Accuse;
import page.clab.api.domain.community.accuse.domain.AccuseStatus;
import page.clab.api.domain.community.accuse.domain.AccuseTarget;
import page.clab.api.domain.community.accuse.domain.TargetType;

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

    public static Accuse toEntity(AccuseRequestDto requestDto, String memberId, AccuseTarget target) {
        return Accuse.builder()
                .memberId(memberId)
                .target(target)
                .reason(requestDto.getReason())
                .isDeleted(false)
                .build();
    }

    public static AccuseTarget toTargetEntity(AccuseRequestDto requestDto) {
        return AccuseTarget.builder()
                .targetType(requestDto.getTargetType())
                .targetReferenceId(requestDto.getTargetId())
                .accuseCount(1L)
                .accuseStatus(AccuseStatus.PENDING)
                .build();
    }
}
