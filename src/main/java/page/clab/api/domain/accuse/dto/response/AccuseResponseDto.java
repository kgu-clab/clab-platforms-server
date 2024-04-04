package page.clab.api.domain.accuse.dto.response;

import lombok.Builder;
import lombok.Getter;
import page.clab.api.domain.accuse.domain.Accuse;
import page.clab.api.domain.accuse.domain.AccuseStatus;
import page.clab.api.domain.accuse.domain.TargetType;

import java.time.LocalDateTime;

@Getter
@Builder
public class AccuseResponseDto {

    private String memberId;

    private String name;

    private TargetType targetType;

    private Long targetId;

    private String reason;

    private AccuseStatus accuseStatus;

    private LocalDateTime createdAt;

    public static AccuseResponseDto toDto(Accuse accuse) {
        return AccuseResponseDto.builder()
                .memberId(accuse.getMember().getId())
                .name(accuse.getMember().getName())
                .targetType(accuse.getTargetType())
                .targetId(accuse.getTargetId())
                .reason(accuse.getReason())
                .accuseStatus(accuse.getAccuseStatus())
                .createdAt(accuse.getCreatedAt())
                .build();
    }

}
